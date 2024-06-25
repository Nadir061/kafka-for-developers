package com.prosoft.config;

import com.prosoft.serde.PersonSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

/**
 * Webinar-06: KafkaConfig содержит конфигурацию для kafka-streams в виде метода getStreamsConfig().
 */
public class KafkaConfig01 {

    public static final String INPUT_TOPIC = "w06-topic1-in";
    public static final String OUTPUT_TOPIC = "w06-topic1-out";

    private KafkaConfig01() {
    }

    public static Properties getStreamsConfig() {
        Properties properties = new Properties();

        /** Ключ конфигурации в Kafka Streams, который указывает уникальный идентификатор для приложения Kafka Streams */
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-transformer");

        /** Адреса Kafka брокеров */
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");

        /** Класс Сериализации/Десериализации (Serde) для ключей */
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());

        /** Класс Сериализации/Десериализации (Serde) для значений */
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, PersonSerde.class.getName());

        return properties;
    }

}
