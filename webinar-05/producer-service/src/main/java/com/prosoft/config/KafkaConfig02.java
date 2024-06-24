package com.prosoft.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

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

        /** Сколько узлов должны подтвердить получение записи, прежде чем считать ее успешно записанной:
         *  - acks=0: продюсер не будет ждать подтверждений от брокера
         *  - acks=1: продюсер будет ждать подтверждения от лидера партиции, но не от всех реплик
         *  - acks=all продюсер будет ждать подтверждений от всех реплик (самая надежная настройка)
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        /** Включение идемпотентности для продюсера */
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        /** Параметр используется для идентификации транзакций. Он назначает уникальный идентификатор транзакционному
         * продюсеру, позволяя ему выполнять и отслеживать транзакции.
         * При использовании транзакций producer должен иметь уникальный TRANSACTIONAL_ID, это Kafka отслеживать состояние
         * транзакций и гарантировать их атомарное выполнение.
         * Для работы с транзакциями Продюсер должен быть Идемпотентным, т.е. enable.idempotence=true
         */
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");

        /** Использование StringSerializer для сериализации ключей и значений сообщений. */
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
