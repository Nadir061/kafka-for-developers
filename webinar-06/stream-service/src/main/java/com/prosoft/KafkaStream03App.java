package com.prosoft;

import com.prosoft.config.KafkaConfig03;
import com.prosoft.domain.Person;
import com.prosoft.serde.PersonSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Webinar-06: Kafka stream-service (variant #3) принимает сообщения из KafkaConfig03.INPUT_TOPIC с типом Person и сортирует
 * с четным возрастом сообщения пересылаются - в KafkaConfig03.OUTPUT_EVEN_AGE_TOPIC,
 * с нечетным возрастом в KafkaConfig03.OUTPUT_ODD_AGE_TOPIC.
 * Подсчет возраста. Это решение позволяет хранить и обновлять счетчики четных и нечетных возрастов в хранилище Kafka Streams,
 * а также выводить их текущие значения в лог при обработке каждого сообщения.
 * Основные изменения:
 * 1) Добавлено создание хранилища calculated-ages-store-name с использованием Stores.keyValueStoreBuilder.
 * 2) Создан новый класс AgeProcessor, который реализует интерфейс Processor для обработки входящих сообщений и обновления хранилища.
 * 3) В методе init процессора инициализируются начальные значения для KEY_STORE_EVEN_AGE и KEY_STORE_ODD_AGE.
 * 4) В методе process процессора обновляются значения в хранилище в зависимости от четности возраста человека.
 * 5) Добавлен вызов inputStream.process(() -> new AgeProcessor(), STORE_NAME) для применения процессора к входящему потоку.
 * 6) Логирование текущих значений счетчиков после каждой обработки.
 * ПРИМЕЧАНИЕ: KafkaStream03App запускается сразу после работы Продюсера (Webinar-06: Kafka producer-service)
 */
public class KafkaStream03App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStream03App.class);
    private static final String STORE_NAME = "calculated-ages-store-name";
    private static final String KEY_STORE_EVEN_AGE = "even";
    private static final String KEY_STORE_ODD_AGE = "odd";

    public static void main(String[] args) {
        Properties config = KafkaConfig03.getStreamsConfig();
        StreamsBuilder builder = new StreamsBuilder();

        /** Создание построителя хранилища для подсчета возрастов */
        StoreBuilder<KeyValueStore<String, Integer>> storeBuilder = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(STORE_NAME),
                Serdes.String(),
                Serdes.Integer()
        );

        /** Добавление хранилища в builder потоков */
        builder.addStateStore(storeBuilder);

        /** Создание входного потока из топика */
        KStream<Long, Person> inputStream = builder.stream(KafkaConfig03.INPUT_TOPIC,
                Consumed.with(Serdes.Long(), new PersonSerde()));

        /** Добавление процессора для подсчета возрастов */
        inputStream.process(() -> new AgeProcessor(), STORE_NAME);

        /** Преобразование входного потока (перевод имени в верхний регистр) */
        KStream<Long, Person> outputStream = inputStream.mapValues(person -> {
            logger.info("Получено: {}", person);
            person.setFirstName(person.getFirstName().toUpperCase());
            return person;
        });

        /** Разделение потока на две ветви по четности возраста */
        KStream<Long, Person>[] branches = outputStream.branch(
                (key, person) -> person.getAge() % 2 == 0,
                (key, person) -> true
        );

        /** Отправка данных с четным возрастом в соответствующий топик */
        branches[0].to(KafkaConfig03.OUTPUT_EVEN_AGE_TOPIC, Produced.with(Serdes.Long(), new PersonSerde()));
        branches[0].print(Printed.<Long, Person>toSysOut().withLabel(String.format("Отправлено в %s", KafkaConfig03.OUTPUT_EVEN_AGE_TOPIC)));

        /** Отправка данных с нечетным возрастом в соответствующий топик */
        branches[1].to(KafkaConfig03.OUTPUT_ODD_AGE_TOPIC, Produced.with(Serdes.Long(), new PersonSerde()));
        branches[1].print(Printed.<Long, Person>toSysOut().withLabel(String.format("Отправлено в %s", KafkaConfig03.OUTPUT_ODD_AGE_TOPIC)));

        /** Создание топологии из builder */
        Topology topology = builder.build();
        logger.info("Топология:\n{}", topology.describe());

        /** Создание и запуск Kafka Streams приложения */
        try (KafkaStreams streams = new KafkaStreams(topology, config)) {
            streams.start();
            /** Добавление хука завершения для корректного закрытия приложения */
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
            /** Блокировка основного потока для поддержания работы приложения */
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            logger.error("Произошла ошибка при работе Kafka Streams", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Процессор для подсчета четных и нечетных возрастов
     */
    static class AgeProcessor implements Processor<Long, Person, Void, Void> {
        private KeyValueStore<String, Integer> kvStore;

        /**
         * Инициализация процессора
         */
        @Override
        public void init(ProcessorContext<Void, Void> context) {
            /** Получение хранилища из контекста */
            kvStore = context.getStateStore(STORE_NAME);
            /** Инициализация начальных значений счетчиков */
            kvStore.putIfAbsent(KEY_STORE_EVEN_AGE, 0);
            kvStore.putIfAbsent(KEY_STORE_ODD_AGE, 0);
        }

        /**
         * Обработка каждой записи
         */
        @Override
        public void process(Record<Long, Person> personRecord) {
            Person person = personRecord.value();
            logger.info("AgeProcessor: person {}, age {}", person.getId(), person.getAge());
            /** Увеличение соответствующего счетчика в зависимости от четности возраста */
            if (person.getAge() % 2 == 0) {
                int evenCount = kvStore.get(KEY_STORE_EVEN_AGE);
                kvStore.put(KEY_STORE_EVEN_AGE, evenCount + 1);
            } else {
                int oddCount = kvStore.get(KEY_STORE_ODD_AGE);
                kvStore.put(KEY_STORE_ODD_AGE, oddCount + 1);
            }
            /** Логирование текущих значений счетчиков */
            logger.info("AgeProcessor: Четное значение: {}", kvStore.get(KEY_STORE_EVEN_AGE));
            logger.info("AgeProcessor: Нечетное значение: {}", kvStore.get(KEY_STORE_ODD_AGE));
        }

        /**
         * Метод закрытия процессора (в данном случае пустой)
         */
        @Override
        public void close() {
            // Здесь можно добавить логику закрытия, если необходимо
        }
    }
}