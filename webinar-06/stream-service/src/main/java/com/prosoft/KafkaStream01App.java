package com.prosoft;

import com.prosoft.config.KafkaConfig01;
import com.prosoft.domain.Person;
import com.prosoft.serde.PersonSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Webinar-06: Kafka stream-service (variant #1)
 * ПРИМЕЧАНИЕ: KafkaStream01App запускается сразу после работы Продюсера (Webinar-06: Kafka producer-service)
 */
public class KafkaStream01App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStream01App.class);

    public static void main(String[] args) {

        /** Получаем конфигурацию Kafka Streams */
        Properties config = KafkaConfig01.getStreamsConfig();

        /** Создаем StreamsBuilder */
        StreamsBuilder builder = new StreamsBuilder();

        /** Вариант записи #1: */

        /** Читаем данные из входного топика */
        KStream<Long, Person> inputStream = builder.stream(KafkaConfig01.INPUT_TOPIC);

        /** Преобразуем данные (переводим строки в верхний регистр) */
        KStream<Long, Person> outputStream = inputStream.mapValues(person -> {
                logger.info("Получено: {}", person);
                person.setFirstName(person.getFirstName().toUpperCase());
                return person;
        });

        /** Отправляем преобразованные данные в KafkaConfig01.OUTPUT_TOPIC */
        outputStream.to(KafkaConfig01.OUTPUT_TOPIC, Produced.with(Serdes.Long(), new PersonSerde()));

        /** Вариант записи #2: более краткая гусеничная запись
         * var outputStream = builder
         *        .stream(KafkaConfig01.INPUT_TOPIC, Consumed.with(Serdes.Long(), new PersonSerde()))
         *        .mapValues(person -> {
         *            logger.info("Получено: {}", person);
         *            person.setFirstName(person.getFirstName().toUpperCase());
         *            return person;
         *        });
         * outputStream.to(KafkaConfig01.OUTPUT_TOPIC, Produced.with(Serdes.Long(), new PersonSerde()));
         */

        /** Вывод данных, которые проходят через поток outputStream, в стандартный вывод (консоль)  */
        outputStream.print(Printed.<Long, Person>toSysOut().withLabel(String.format("Отправлено в %s", KafkaConfig01.OUTPUT_TOPIC)));

        /** Получаем топологию */
        Topology topology = builder.build();
        logger.info("Топология:\n{}", topology.describe());

        /** Использование try-with-resources для автоматического закрытия KafkaStreams */
        try (KafkaStreams streams = new KafkaStreams(builder.build(), config)) {

            /** Запуск приложения Kafka Streams */
            streams.start();

            /** Добавить Shutdown hook для корректного завершения приложения */
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

            /** Блокировка главного потока, для того чтобы приложение оставалось активным после запуска KafkaStreams */
            Thread.sleep(Long.MAX_VALUE);

        } catch (Exception e) {
            logger.error("Произошла ошибка при работе Kafka Streams", e);
            Thread.currentThread().interrupt();
        }
    }

}