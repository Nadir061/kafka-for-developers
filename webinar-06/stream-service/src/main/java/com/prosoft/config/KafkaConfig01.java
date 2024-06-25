package com.prosoft.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

/**
 * Webinar-06: KafkaConfig содержит конфигурацию для kafka-streams в виде метода getStreamsConfig().
 */
public class KafkaConfig01 {

    public static final String INPUT_TOPIC = "w06-topic1-in";
    public static final String OUTPUT_TOPIC = "w06-topic1-out";

    public static Properties getStreamsConfig() {
        Properties properties = new Properties();

        /** Ключ конфигурации в Kafka Streams, который указывает уникальный идентификатор для приложения Kafka Streams */
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-transformer");

        /** Адреса Kafka брокеров */
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");

        /** Классы Сериализации/Десериализации (Serde) */
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        return properties;
    }

}
