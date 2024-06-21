package com.prosoft;

import com.prosoft.config.KafkaConfig01;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

/**
 * Webinar-02: Kafka consumer-service (прием данных типа String )
 * Использования метода consumer.poll().
 */
public class KafkaConsumer01App {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer01App.class);
    private static final Duration TEN_MILLISECONDS_INTERVAL = Duration.ofMillis(10);

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(KafkaConfig01.getConsumerConfig());
        try (consumer) {
            consumer.subscribe(Collections.singletonList(KafkaConfig01.TOPIC));
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(TEN_MILLISECONDS_INTERVAL);
                for (ConsumerRecord<String, String> cr : consumerRecords) {
                    logger.info("topic = {}, offset = {}, key = {}, value = {}", cr.topic(), cr.offset(), cr.key(), cr.value());
                }
            }
        }
    }
}