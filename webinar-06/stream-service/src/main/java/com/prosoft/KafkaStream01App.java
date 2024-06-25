package com.prosoft;

import com.prosoft.config.KafkaConfig01;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Webinar-06: Kafka stream-service (variant #1)
 */
public class KafkaStream01App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStream01App.class);

    public static void main(String[] args) {

        /** Получаем конфигурацию Kafka Streams */
        Properties config = KafkaConfig01.getStreamsConfig();

        /** Создаем StreamsBuilder */
        StreamsBuilder builder = new StreamsBuilder();

        /** Читаем данные из входного топика */
        KStream<String, String> inputStream = builder.stream(KafkaConfig01.INPUT_TOPIC);

        /** Преобразуем данные (переводим строки в верхний регистр) */
        KStream<String, String> outputStream = inputStream.mapValues(value -> {
            logger.info("Получено: {}", value);
            return value.toUpperCase(); }
        );

        /** Переменная для подсчета отправленных сообщений */
        AtomicLong sentCount = new AtomicLong(0);

        /** Отправляем преобразованные данные в выходной Топик */
        outputStream.to(KafkaConfig01.OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String())
                .withStreamPartitioner((topic, key, value, numPartitions) -> {
                    logger.info("Отправлено: {} в топик {}", value, topic);
                    sentCount.incrementAndGet();
                    /** Используем 0, чтобы отправить в партицию 0 */
                    return 0;
                }));

        /** Используем try-with-resources для автоматического закрытия KafkaStreams */
        try (KafkaStreams streams = new KafkaStreams(builder.build(), config)) {

            /** Запускаем приложение Kafka Streams */
            streams.start();

            /** Добавляем Shutdown hook для корректного завершения приложения */
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

            /** Блокируем главный поток, чтобы приложение оставалось активным после запуска KafkaStreams */
            Thread.sleep(Long.MAX_VALUE);

        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

}