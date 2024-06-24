package com.prosoft;

import com.prosoft.config.KafkaConfig02;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Webinar-05: Kafka producer-service (variant #2)
 * Использования метода producer.send(producerRecord) без обработки результата.
 */
public class KafkaProducer02App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer02App.class);
    private static final int MAX_MESSAGE = 10;

    public static void main(String[] args) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig02.getProducerConfig())) {

            /** init transaction */
            producer.initTransactions();

            for (int i = 0; i < MAX_MESSAGE; i++) {

                try {
                    /** start transaction */
                    producer.beginTransaction();

                    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(KafkaConfig02.TOPIC, "key-" + i, "value-" + i);
                    producer.send(producerRecord);
                    logger.info("Отправлено сообщение: key-{}, value-{}", i, i);

                    /** commit transaction */
                    producer.commitTransaction();
                } catch (Exception e) {
                    logger.error("Ошибка при отправке сообщения в Kafka", e);

                    /** abort transaction on error */
                    producer.abortTransaction();
                }
            }
            logger.info("Отправка завершена.");

        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщений в Kafka", e);
        }
    }
}
