package com.prosoft;

import com.prosoft.config.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

public class KafkaConsumerApp {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerApp.class);

    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(KafkaConfig.getConsumerConfig());
        try (consumer) {
            consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC));
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(10));
                for (ConsumerRecord<String, String> cr : consumerRecords) {
                    logger.info("topic = {}, offset = {}, key = {}, value = {}", cr.topic(), cr.offset(), cr.key(), cr.value());
                }
            }
        }
    }

}