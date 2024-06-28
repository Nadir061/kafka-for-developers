package com.prosoft.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;

import java.util.Properties;

/**
 * Webinar-07: KafkaConfig содержит конфигурацию для кансамера в виде метода getConsumerConfig.
 */
public class KafkaConfig {

    public static final String TOPIC = "w7-topic";

    private static final String BOOTSTRAP_SERVERS = "localhost:9093";
    private static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";

    private static final String GROUP_ID = "my-consumer-group";

    private KafkaConfig() {
    }

    public static Properties getConsumerConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Идентификатор группы потребителей (consumer group ID) */
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        /** Использование LongDeserializer для десериализации ключей (Key) */
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());

        /** Использование KafkaAvroDeserializer для десериализации значения (Value) в формате Avro позволяет автоматически
         *  загружать схемы из Schema Registry. Зависимость kafka-avro-serializer (7.4.0) из репозитория https://packages.confluent.io/maven/ */
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());

        /** Адрес Schema Registry */
        properties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);

        /** Настройка десериализатора (Avro) использовать сгенерированные классы (specific classes) для десериализации Avro-сообщений */
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        return properties;
    }
}
