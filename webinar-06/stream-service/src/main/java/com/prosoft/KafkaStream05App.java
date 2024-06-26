package com.prosoft;

import com.prosoft.config.KafkaConfig05;
import com.prosoft.domain.Person;
import com.prosoft.serde.PersonSerde;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

/**
 * Webinar-06: Kafka stream-service (variant #5)
 * Чтение данных из KafkaConfig05.OUTPUT_EVEN_AGE_TOPIC и KafkaConfig05.OUTPUT_ODD_AGE_TOPIC и объединение через .outerJoin
 * в KafkaConfig05.OUTPUT_EVEN_AND_ODD_AGE_TOPIC и добавим Timestamp из возраста объекта Person.
 * ПРИМЕЧАНИЕ: KafkaStream05App запускается после KafkaStream03App.
 */
public class KafkaStream05App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStream05App.class);

    public static void main(String[] args) {

        /** Получаем конфигурацию Kafka Streams */
        Properties config = KafkaConfig05.getStreamsConfig();

        /** Создаем StreamsBuilder */
        StreamsBuilder builder = new StreamsBuilder();

        /** Создаем пользовательский TimestampExtractor */
        TimestampExtractor personTimestampExtractor = new TimestampExtractor() {
            @Override
            public long extract(ConsumerRecord<Object, Object> personRecord, long partitionTime) {
                Person person = (Person) personRecord.value();
                return person.getAge() * 1000L;
            }
        };

        /** Читаем данные из топиков с четным и нечетным возрастом и применяем TimestampExtractor при чтении данных */
        KStream<Long, Person> evenAgeInputStream = builder.stream(
                KafkaConfig05.OUTPUT_EVEN_AGE_TOPIC,
                Consumed.with(Serdes.Long(), new PersonSerde()).withTimestampExtractor(personTimestampExtractor)
        );
        KStream<Long, Person> oddAgeInputStream = builder.stream(
                KafkaConfig05.OUTPUT_ODD_AGE_TOPIC,
                Consumed.with(Serdes.Long(), new PersonSerde()).withTimestampExtractor(personTimestampExtractor)
        );


        /** Выполняем Join потоков */
        KStream<Long, String> joinedStream = evenAgeInputStream.outerJoin(oddAgeInputStream,
                (evenPerson, oddPerson) -> {
                    if (evenPerson != null && oddPerson != null) {
                        return "Even: " + evenPerson.toString() + ", Odd: " + oddPerson.toString();
                    } else if (evenPerson != null) {
                        return "Even: " + evenPerson.toString();
                    } else if (oddPerson != null) {
                        return "Odd: " + oddPerson.toString();
                    } else {
                        return "No data";
                    }
                },
                JoinWindows.of(Duration.ofMinutes(5)));

        /** Отправляем результат Join в новый топик */
        joinedStream.to(KafkaConfig05.OUTPUT_EVEN_AND_ODD_AGE_TOPIC, Produced.with(Serdes.Long(), Serdes.String()));

        /** Выводим результат Join в консоль  */
        joinedStream.print(Printed.<Long, String>toSysOut().withLabel("Joined Result"));

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
