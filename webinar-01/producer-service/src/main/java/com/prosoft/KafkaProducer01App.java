package com.prosoft;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.prosoft.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka: producer-service (variant #1)
 * Использования метода producer.send(producerRecord) без обработки результата.
 */
public class KafkaProducer01App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer01App.class);

    public static void main(String[] args) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig.getProducerConfig())) {
            for (int i = 0; i < 10; i++) {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(KafkaConfig.TOPIC, "key-" + i, "value-" + i);
                producer.send(producerRecord);
                logger.info("Отправлено сообщение: key-{}, value-{}", i, i);
            }
            logger.info("Отправка завершена.");
        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщений в Kafka", e);
        }
    }

}
