package com.prosoft;

import com.prosoft.config.KafkaConfig02;
import com.prosoft.domain.Person;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Webinar-02: Kafka producer-service (variant #1) (отправка объектов класса Person)
 * Использования метода producer.send(producerRecord) без обработки результата.
 */
public class KafkaProducer02App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer02App.class);
    private static final int MAX_MESSAGE = 10;

    public static void main(String[] args) {
        try (KafkaProducer<Long, Person> producer = new KafkaProducer<>(KafkaConfig02.getProducerConfig())) {

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
                ProducerRecord<Long, Person> producerRecord = new ProducerRecord<>(KafkaConfig02.TOPIC, KafkaConfig02.PARTITION,
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
        return new Person(index, "FirstName" + index, "LastName" + index, 20 + index);
    }

}
