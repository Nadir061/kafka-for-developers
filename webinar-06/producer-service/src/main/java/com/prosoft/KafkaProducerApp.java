package com.prosoft;

import com.prosoft.config.KafkaConfig;
import com.prosoft.domain.Person;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Webinar-06: Kafka producer-service (отправка объектов класса Person)
 */
public class KafkaProducerApp {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerApp.class);
    private static final int MAX_MESSAGE = 10;

    public static void main(String[] args) {
        try (KafkaProducer<Long, Person> producer = new KafkaProducer<>(KafkaConfig.getProducerConfig())) {

            for (int i = 0; i < MAX_MESSAGE; i++) {
                Person person = createPerson(i);

                /** Конструктор ProducerRecord(topic, key, value) */
                ProducerRecord<Long, Person> producerRecord = new ProducerRecord<>(KafkaConfig.TOPIC, person.getId(), person);

                /** Отправка сообщения */
                producer.send(producerRecord);

                logger.info("Отправлено сообщение: key-{}, value-{}", person.getId(), person);
            }
        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщений в Kafka", e);
        }
    }

    private static Person createPerson(int index) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
        return new Person(index, "FirstName-" + currentTime, "LastName" + index, 20 + index);
    }

}
