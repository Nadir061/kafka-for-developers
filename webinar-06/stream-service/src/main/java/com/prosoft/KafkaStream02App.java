package com.prosoft;

import com.prosoft.config.KafkaConfig02;
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
 * Webinar-06: Kafka stream-service (variant #2) принимает сообщения из KafkaConfig02.INPUT_TOPIC с типом Person и сортирует
 * с четным возрастом сообщения пересылаются - в KafkaConfig02.OUTPUT_EVEN_AGE_TOPIC,
 * с нечетным возрастом в KafkaConfig02.OUTPUT_ODD_AGE_TOPIC
 * ПРИМЕЧАНИЕ: KafkaStream02App запускается сразу после работы Продюсера (Webinar-06: Kafka producer-service)
 */
public class KafkaStream02App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStream02App.class);

    public static void main(String[] args) {

        /** Получаем конфигурацию Kafka Streams */
        Properties config = KafkaConfig02.getStreamsConfig();

        /** Создаем StreamsBuilder */
        StreamsBuilder builder = new StreamsBuilder();

        /** Читаем данные из входного топика */
        KStream<Long, Person> inputStream = builder.stream(KafkaConfig02.INPUT_TOPIC);

        /** Преобразуем данные (переводим строки в верхний регистр) */
        KStream<Long, Person> outputStream = inputStream.mapValues(person -> {
                logger.info("Получено: {}", person);
                person.setFirstName(person.getFirstName().toUpperCase());
                return person;
        });

        /** Фильтрация и отправка в разные топики в зависимости от четности возраста через разделение потока на две ветви по четности возраста */
        KStream<Long, Person>[] branches = outputStream.branch(
                (key, person) -> person.getAge() % 2 == 0,
                (key, person) -> true
        );

        /** Отправляем данные с четным возрастом в OUTPUT_TOPIC_1 */
        branches[0].to(KafkaConfig02.OUTPUT_EVEN_AGE_TOPIC, Produced.with(Serdes.Long(), new PersonSerde()));
        branches[0].print(Printed.<Long, Person>toSysOut().withLabel(String.format("Отправлено в %s", KafkaConfig02.OUTPUT_EVEN_AGE_TOPIC)));

        /** Отправляем данные с нечетным возрастом в OUTPUT_TOPIC_2 */
        branches[1].to(KafkaConfig02.OUTPUT_ODD_AGE_TOPIC, Produced.with(Serdes.Long(), new PersonSerde()));
        branches[1].print(Printed.<Long, Person>toSysOut().withLabel(String.format("Отправлено в %s", KafkaConfig02.OUTPUT_ODD_AGE_TOPIC)));


        /** Создание топологии из builder */
        Topology topology = builder.build();
        logger.info("Топология:\n{}", topology.describe());

        /** Использование try-with-resources для автоматического закрытия KafkaStreams */
        try (KafkaStreams streams = new KafkaStreams(builder.build(), config)) {

            /** Запуск приложения Kafka Streams */
            streams.start();

            /** Добавление хука (Shutdown hook - выполнение кода при завершении работы Java Virtual Machine (JVM) */
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

            /** Блокировка главного потока, для того чтобы приложение оставалось активным после запуска KafkaStreams */
            Thread.sleep(Long.MAX_VALUE);

        } catch (Exception e) {
            logger.error("Произошла ошибка при работе Kafka Streams", e);
            Thread.currentThread().interrupt();
        }
    }

}