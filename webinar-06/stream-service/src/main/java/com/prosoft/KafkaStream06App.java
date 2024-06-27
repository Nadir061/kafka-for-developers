package com.prosoft;

import com.prosoft.config.KafkaConfig06;
import com.prosoft.domain.Person;
import com.prosoft.serde.PersonSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Webinar-06: Kafka stream-service (variant #6)
 * ПРИМЕЧАНИЕ: KafkaStream06App запускается сразу после работы Продюсера (Webinar-06: Kafka producer-service)
 */
public class KafkaStream06App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStream06App.class);

    public static void main(String[] args) {

        Properties config = KafkaConfig06.getStreamsConfig();
        StreamsBuilder builder = new StreamsBuilder();

        /** Создание KTable из входного топика */
        KTable<Long, Person> inputTable = builder.table(KafkaConfig06.INPUT_TOPIC,
                Consumed.with(Serdes.Long(), new PersonSerde()));

        /** Вывод содержимого KTable в лог при каждом обновлении */
        inputTable.toStream().foreach((key, value) ->
                logger.info("Обновление в KTable: Key = {}, Value = {}", key, value));

        /** Создание и вывод информации о топологии Kafka Streams */
        Topology topology = builder.build();
        logger.info("Топология:\n{}", topology.describe());

        try (KafkaStreams streams = new KafkaStreams(topology, config)) {
            streams.start();
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            logger.error("Произошла ошибка при работе Kafka Streams", e);
            Thread.currentThread().interrupt();
        }

    }

}
