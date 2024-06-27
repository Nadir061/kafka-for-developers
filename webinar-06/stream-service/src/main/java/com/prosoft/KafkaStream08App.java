package com.prosoft;

import com.prosoft.config.KafkaConfig08;
import com.prosoft.domain.Person;
import com.prosoft.serde.PersonSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Webinar-06: Kafka stream-service (variant #8) Topology Processor API
 * ПРИМЕЧАНИЕ: KafkaStream08App запускается сразу после работы Продюсера (Webinar-06: Kafka producer-service)
 */
public class KafkaStream08App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStream08App.class);

    public static void main(String[] args) {

        /** Получаем конфигурацию Kafka Streams */
        Properties config = KafkaConfig08.getStreamsConfig();

        /** Создаем топологию */
        Topology topology = new Topology();

        /** Определяем источник (source) */
        topology.addSource("Source", Serdes.Long().deserializer(), new PersonSerde().deserializer(), KafkaConfig08.INPUT_TOPIC);

        /** Определяем процессор (processor) для обработки данных */
        topology.addProcessor("Processor", new ProcessorSupplier<Long, Person>() {
            @Override
            public AbstractProcessor<Long, Person> get() {
                return new AbstractProcessor<Long, Person>() {
                    @Override
                    public void process(Long key, Person value) {
                        logger.info("Получено: {}", value);
                        if (value != null) {
                            value.setFirstName(value.getFirstName().toUpperCase());
                            context().forward(key, value);
                        }
                        context().commit();
                    }
                };
            }
        }, "Source");

        /** Определяем вывод (sink) для отправки обработанных данных в выходной топик */
        topology.addSink("Sink", KafkaConfig08.OUTPUT_TOPIC, Serdes.Long().serializer(), new PersonSerde().serializer(), "Processor");

        /** Выводим топологию */
        logger.info("Топология:\n{}", topology.describe());

        /** Использование try-with-resources для автоматического закрытия KafkaStreams */
        try (KafkaStreams streams = new KafkaStreams(topology, config)) {

            /** Запуск приложения Kafka Streams */
            streams.start();

            /** Добавить Shutdown hook для корректного завершения приложения */
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

            /** Блокировка главного потока, для того чтобы приложение оставалось активным после запуска KafkaStreams */
            Thread.sleep(Long.MAX_VALUE);

        } catch (Exception e) {
            logger.error("Произошла ошибка при работе Kafka Streams", e);
            Thread.currentThread().interrupt();
        }

    }

}
