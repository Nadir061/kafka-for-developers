package com.prosoft.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.TRANSACTIONAL_ID_CONFIG;

/**
 * Webinar-05: KafkaConfig содержит конфигурацию для продюсера в виде метода getProducerConfig.
 * Конфигурации включают настройки для серверов Kafka, сериализации и групп отправителей.
 */
public class KafkaConfig02 {

    public static final String TOPIC = "topic1";

    private static final String BOOTSTRAP_SERVERS = "localhost:9094";

    private KafkaConfig02() { }

    public static Properties getProducerConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Включение идемпотентности для продюсера */
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        /** Параметр используется для идентификации транзакций. Он назначает уникальный идентификатор транзакционному
         * продюсеру, позволяя ему выполнять и отслеживать транзакции.
         */
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");

        /** Сколько узлов должны подтвердить получение записи, прежде чем считать ее успешно записанной:
         *  - acks=0: продюсер не будет ждать подтверждений от брокера
         *  - acks=1: продюсер будет ждать подтверждения от лидера партиции, но не от всех реплик
         *  - acks=all продюсер будет ждать подтверждений от всех реплик (самая надежная настройка)
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        /** Использование StringSerializer для сериализации ключей и значений сообщений.
         *  StringSerializer.class в контексте Apache Kafka представляет собой реализацию интерфейса Serializer
         *  из клиентской библиотеки Kafka, которая используется для сериализации объектов типа String в байтовый формат.
         */
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
