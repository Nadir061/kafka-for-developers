package com.prosoft.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * KafkaConfig содержит конфигурацию для продюсера в виде метода getProducerConfig.
 * Конфигурации включают настройки для серверов Kafka, сериализации/десериализации и групп потребителей.
 */
public class KafkaConfig {

    public static final String TOPIC = "topic1";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    private KafkaConfig() { }

    public static Properties getProducerConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру (localhost:9092) */
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Использование StringSerializer для сериализации ключей и значений сообщений.
         *  StringSerializer.class в контексте Apache Kafka представляет собой реализацию интерфейса Serializer
         *  из клиентской библиотеки Kafka, которая используется для сериализации объектов типа String в байтовый формат.
         */
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
