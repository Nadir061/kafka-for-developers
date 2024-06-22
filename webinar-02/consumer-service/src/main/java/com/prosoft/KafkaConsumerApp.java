package com.prosoft;

import com.prosoft.config.KafkaConfig;
import com.prosoft.domain.Person;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

/**
 * Webinar-02: Kafka consumer-service (прием экземпляров класса Person из topic2)
 * Использования метода consumer.poll().
 */
public class KafkaConsumerApp {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerApp.class);
    private static final Duration TEN_MILLISECONDS_INTERVAL = Duration.ofMillis(10);

    public static void main(String[] args) {
        KafkaConsumer<Long, Person> consumer = new KafkaConsumer<>(KafkaConfig.getConsumerConfig());
        try (consumer) {
            consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC));
            while (true) {
                ConsumerRecords<Long, Person> consumerRecords = consumer.poll(TEN_MILLISECONDS_INTERVAL);
                for (ConsumerRecord<Long, Person> cr : consumerRecords) {
                    logger.info("Received record: key={}, value={}, partition={}, offset={}",
                            cr.key(), cr.value(), cr.partition(), cr.offset());
                }
            }
        }
    }
}
