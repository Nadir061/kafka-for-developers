package com.prosoft;

import com.prosoft.config.KafkaConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Webinar-01: Kafka producer-service (variant #2)
 * Использования метода send(ProducerRecord<K, V> record) с возвращаемым значением Future<RecordMetadata>,
 */
public class KafkaProducer02App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer02App.class);
    private static final int MAX_MESSAGE = 10;

    public static void main(String[] args) {

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig.getProducerConfig())) {
            for (int i = 0; i < MAX_MESSAGE; i++) {

                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(KafkaConfig.TOPIC, "key-" + i, "value-" + i);

                /**
                 * Отправка сообщения в топик KafkaConfig.TOPIC с использованием метода send для получения Future<RecordMetadata>.
                 * Метод get вызывается на Future для получения метаданных отправленного сообщения, таких как раздел и смещение.
                 * Класс ExecutionException используется для обработки исключений при выполнении асинхронных задач
                 * (с классами из пакета java.util.concurrent, такими как Future и ExecutorService).
                 */
                Future<RecordMetadata> future = producer.send(producerRecord);
                try {
                    RecordMetadata metadata = future.get();
                    logger.info("Sent record(key={}, value={}) meta(partition={}, offset={})",
                            producerRecord.key(), producerRecord.value(),
                            metadata.partition(), metadata.offset());
                } catch (ExecutionException | InterruptedException e) {
                    logger.error("Error sending message to Kafka", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
