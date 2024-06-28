package com.prosoft.config;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;

/**
 * Webinar-07: KafkaConfig содержит конфигурацию для продюсера в виде метода getProducerConfig.
 */
public class KafkaConfig {

    public static final String TOPIC = "w7-topic";

    private static final String BOOTSTRAP_SERVERS = "localhost:9093";

    private KafkaConfig() {
    }

    public static Properties getProducerConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Сколько узлов должны подтвердить получение записи: acks=all продюсер будет ждать подтверждений от всех реплик */
        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        /** Использование LongSerializer для сериализации ключа (Key) */
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());

        /** Использование KafkaAvroSerializer для сериализации значения (Value) в формате Avro позволяет автоматически
         *  загружать схемы из Schema Registry.
         *  Зависимость kafka-avro-serializer (7.4.0) из репозитория https://packages.confluent.io/maven/
         */
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());

        /** Адрес Schema Registry */
        properties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        return properties;
    }

}
