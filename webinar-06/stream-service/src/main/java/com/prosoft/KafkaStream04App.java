package com.prosoft;

import com.prosoft.config.KafkaConfig04;
import com.prosoft.domain.Person;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

/**
 * Webinar-06: Kafka stream-service (variant #4)
 * Чтение данных из KafkaConfig04.OUTPUT_EVEN_AGE_TOPIC и KafkaConfig04.OUTPUT_ODD_AGE_TOPIC и объединение через .outerJoin
 * в KafkaConfig04.OUTPUT_EVEN_AND_ODD_AGE_TOPIC
 * ПРИМЕЧАНИЕ: KafkaStream04App запускается после KafkaStream03App.
 */
public class KafkaStream04App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStream04App.class);

    public static void main(String[] args) {

        /** Получаем конфигурацию Kafka Streams */
        Properties config = KafkaConfig04.getStreamsConfig();

        /** Создаем StreamsBuilder */
        StreamsBuilder builder = new StreamsBuilder();

        /** Читаем данные из топиков с четным и нечетным возрастом */
        KStream<Long, Person> evenAgeInputStream = builder.stream(KafkaConfig04.OUTPUT_EVEN_AGE_TOPIC);
        KStream<Long, Person> oddAgeInputStream = builder.stream(KafkaConfig04.OUTPUT_ODD_AGE_TOPIC);

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
        joinedStream.to(KafkaConfig04.OUTPUT_EVEN_AND_ODD_AGE_TOPIC, Produced.with(Serdes.Long(), Serdes.String()));

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