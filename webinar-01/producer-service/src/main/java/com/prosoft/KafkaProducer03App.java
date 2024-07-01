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
 * Webinar-01: Kafka producer-service (variant #3)
 * Используется метод send(ProducerRecord<K, V> record, Callback callback) с Future<RecordMetadata> и Callback
 */
public class KafkaProducer03App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer03App.class);
    private static final int MAX_MESSAGE = 10;

    public static void main(String[] args) {

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaConfig.getProducerConfig())) {
            for (int i = 0; i < MAX_MESSAGE; i++) {

                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(KafkaConfig.TOPIC, "key-" + i, "value-" + i);

                /**
                 * Отправка сообщения в тему KafkaConfig.TOPIC с использованием метод send с Callback.
                 *
                 * @return Future объект, представляющий асинхронный результат отправки сообщения.
                 */
                Future<RecordMetadata> future = producer.send(producerRecord, (metadata, exception) -> {
                    if (exception != null) {
                        logger.error("Error sending record(key={}, value={})", producerRecord.key(),
                                producerRecord.value(), exception);
                    } else {
                        logger.info("Sent record(key={}, value={}) meta(partition={}, offset={})",
                                producerRecord.key(), producerRecord.value(),
                                metadata.partition(), metadata.offset());
                    }
                });

                /**
                 * Опция: Future<RecordMetadata> используется для синхронного получения метаданных отправленного сообщения
                 * и обработки возможных ошибок.
                 * Класс ExecutionException используется для обработки исключений при выполнении асинхронных задач
                 * (с классами из пакета java.util.concurrent, такими как Future и ExecutorService).
                 */
                try {
                    RecordMetadata metadata = future.get();
                    logger.info("Record sent to partition {} with offset {}", metadata.partition(), metadata.offset());
                } catch (ExecutionException | InterruptedException e) {
                    logger.error("Error while waiting for future", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
