package com.prosoft.config;

import com.prosoft.serde.PersonSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

/**
 * Webinar-06: KafkaConfig для KafkaStream04App содержит конфигурацию для kafka-streams в виде метода getStreamsConfig().
 */
public class KafkaConfig04 {

    /** Топик для Person c четным возрастом */
    public static final String OUTPUT_EVEN_AGE_TOPIC = "w06-topic-out-even-age";
    /** Топик для Person c нечетным возрастом */
    public static final String OUTPUT_ODD_AGE_TOPIC = "w06-topic-out-odd-age";

    /** Топик для Person c объединением через Join четного и нечетного возраста */
    public static final String OUTPUT_EVEN_AND_ODD_AGE_TOPIC = "w06-topic-out-even-and-odd-age";

    private KafkaConfig04() {
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
