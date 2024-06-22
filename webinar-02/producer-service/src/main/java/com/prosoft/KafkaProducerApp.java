package com.prosoft;

import com.prosoft.config.KafkaConfig;
import com.prosoft.domain.Person;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Webinar-02: Kafka producer-service (variant #1) (отправка объектов класса Person)
 * Использования метода producer.send(producerRecord) без обработки результата.
 */
public class KafkaProducerApp {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerApp.class);
    private static final int MAX_MESSAGE = 10;

    public static void main(String[] args) {
        try (KafkaProducer<Long, Person> producer = new KafkaProducer<>(KafkaConfig.getProducerConfig())) {

            for (int i = 0; i < MAX_MESSAGE; i++) {
                Person person = createPerson(i);

                /**
                 * Конструктор ProducerRecord(topic, partition, timestamp, key, value) принимает в качестве аргументов:
                 * - topic - номер топика
                 * - partition - номер партиции           (опция)
                 * - timestamp - время создания сообщения (опция)
                 * - key - ключ id экземпляра Person      (опция)
                 * - value - объект Person
                 *
                 * Варианты конструкторов:
                 * - ProducerRecord(topic, value)
                 * - ProducerRecord(topic, key, value)
                 * - ProducerRecord(topic, partition, key, value)
                 * - ProducerRecord(topic, partition, key, value, headers)
                 */
                long timestamp = System.currentTimeMillis();
                ProducerRecord<Long, Person> producerRecord = new ProducerRecord<>(KafkaConfig.TOPIC, KafkaConfig.PARTITION,
                        timestamp, person.getId(), person);

                /**
                 * Анонимный внутренний класс (Callback), содержащий только один метод onCompletion(), можно записать
                 * через лямбду
                 */
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null) {
                            logger.error("Error sending message: {}", e.getMessage(), e);
                        } else {
                            logger.info("Sent record: key={}, value={}, partition={}, offset={}",
                                    person.getId(), person, recordMetadata.partition(), recordMetadata.offset());                        }
                    }
                });
                logger.info("Отправлено сообщение: key-{}, value-{}", i, person);
            }
            logger.info("Отправка завершена.");
        } catch (Exception e) {
            logger.error("Ошибка при отправке сообщений в Kafka", e);
        }
    }

    private static Person createPerson(int index) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
        return new Person(index, "FirstName-" + currentTime, "LastName" + index, 20 + index);
    }

}
