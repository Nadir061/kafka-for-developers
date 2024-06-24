package com.prosoft;

import com.prosoft.config.KafkaConfig02;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

/**
 * Webinar-05 Kafka: Транзакционный consumer-service
 * Использования метода consumer.poll().
 */
public class KafkaConsumer02App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer02App.class);
    private static final Duration TEN_MILLISECONDS_INTERVAL = Duration.ofMillis(10);

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(KafkaConfig02.getConsumerConfig());
        try (consumer) {
            consumer.subscribe(Collections.singletonList(KafkaConfig02.TOPIC));
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(TEN_MILLISECONDS_INTERVAL);
                for (ConsumerRecord<String, String> cr : consumerRecords) {
                    logger.info("topic = {}, key = {}, value = {}, offset = {}", cr.topic(), cr.key(), cr.value(), cr.offset());
                }
            }
        }
    }

}