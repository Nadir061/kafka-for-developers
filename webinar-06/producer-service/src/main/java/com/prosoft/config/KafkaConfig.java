package com.prosoft.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * Webinar-06: KafkaConfig содержит конфигурацию для продюсера в виде метода getProducerConfig.
 */
public class KafkaConfig {

    public static final String TOPIC = "w06-topic1-in";

    private static final String BOOTSTRAP_SERVERS = "localhost:9093";

    private KafkaConfig() { }

    public static Properties getProducerConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Сколько узлов должны подтвердить получение записи, прежде чем считать ее успешно записанной:
         *  - acks=0: продюсер не будет ждать подтверждений от брокера
         *  - acks=1: продюсер будет ждать подтверждения от лидера партиции, но не от всех реплик
         *  - acks=all продюсер будет ждать подтверждений от всех реплик (самая надежная настройка)
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        /** Установка размера пакета в ноль, чтобы отправка была сразу, без буферизации */
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "0");

        /** Использование StringSerializer для сериализации ключей и значений сообщений.
         *  StringSerializer.class в контексте Apache Kafka представляет собой реализацию интерфейса Serializer
         *  из клиентской библиотеки Kafka, которая используется для сериализации объектов типа String в байтовый формат.
         */
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
