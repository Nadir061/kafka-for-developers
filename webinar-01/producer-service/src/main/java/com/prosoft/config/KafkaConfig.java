package com.prosoft.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * KafkaConfig содержит конфигурацию для продюсера в виде метода getProducerConfig.
 * Конфигурации включают настройки для серверов Kafka, сериализации/десериализации и групп отправителей.
 */
public class KafkaConfig {

    public static final String TOPIC = "topic1";

    // (1) Err: Продюсер отправляет, а Консамер не получает. Kafdrop работает на http://localhost:9000/
    // docker-compose.yaml
    // private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    // (2) Продюсер отправляет, а Консамер получает. Kafdrop работает на http://localhost:9000/ Есть ошибки в zookeeper в Docker. Но в целом все ок!!!
    // docker-compose2.yaml
    // private static final String BOOTSTRAP_SERVERS = "localhost:9091";

    // (3) Продюсер отправляет, а Консамер получает. Kafdrop работает на http://localhost:9000/ Ошибок нет в Docker
    // docker-compose3.yaml
    // private static final String BOOTSTRAP_SERVERS = "localhost:9093";

    // (4) Продюсер отправляет, а Консамер получает. Kafdrop работает на http://localhost:9000/ Ошибок нет в Docker. Версии не посследние перед latest
    // docker-compose4.yaml
    private static final String BOOTSTRAP_SERVERS = "localhost:9093";

    private KafkaConfig() { }

    public static Properties getProducerConfig() {
        Properties properties = new Properties();

        /** Подключения к Kafka-брокеру BOOTSTRAP_SERVERS */
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        /** Использование StringSerializer для сериализации ключей и значений сообщений.
         *  StringSerializer.class в контексте Apache Kafka представляет собой реализацию интерфейса Serializer
         *  из клиентской библиотеки Kafka, которая используется для сериализации объектов типа String в байтовый формат.
         */
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }

}
