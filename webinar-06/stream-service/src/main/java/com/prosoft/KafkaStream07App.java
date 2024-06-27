package com.prosoft;

import com.prosoft.config.KafkaConfig07;
import com.prosoft.domain.Person;
import com.prosoft.serde.PersonSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Webinar-06: Kafka stream-service (variant #7) GlobalKTable
 * ПРИМЕЧАНИЕ: KafkaStream07App запускается после KafkaStream03App
 */
public class KafkaStream07App {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStream07App.class);


    public static void main(String[] args) {

        Properties config = KafkaConfig07.getStreamsConfig();

        StreamsBuilder builder = new StreamsBuilder();

        /** Создаем GlobalKTable из топика с четным возрастом */
        GlobalKTable<Long, Person> evenAgePersons = builder.globalTable(
                KafkaConfig07.INPUT_EVEN_AGE_TOPIC,
                Consumed.with(Serdes.Long(), new PersonSerde())
        );

        /** Создаем KStream из топика с нечетным возрастом */
        KStream<Long, Person> oddAgePersons = builder.stream(
                KafkaConfig07.INPUT_ODD_AGE_TOPIC,
                Consumed.with(Serdes.Long(), new PersonSerde())
        );

        /** Объединяем данные из KStream (нечетный возраст) и GlobalKTable (четный возраст) */
        KStream<Long, Person> mergedStream = oddAgePersons.merge(
                oddAgePersons.leftJoin(evenAgePersons,
                                (personId, oddAgePerson) -> personId,
                                (oddAgePerson, evenAgePerson) -> evenAgePerson)
                        .filter((key, value) -> value != null)
        );

        /**  Отправляем все сообщения в выходной топик */
        mergedStream.to(KafkaConfig07.OUTPUT_TOPIC, Produced.with(Serdes.Long(), new PersonSerde()));

        /** Создаем объект KafkaStreams, передавая ему построенную топологию и конфигурацию */
        KafkaStreams streams = new KafkaStreams(builder.build(), config);

        /** Выводим топологию */
        logger.info("Topology: \n {}", streams);

        streams.start();

        /** Добавляем хук завершения */
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

}
