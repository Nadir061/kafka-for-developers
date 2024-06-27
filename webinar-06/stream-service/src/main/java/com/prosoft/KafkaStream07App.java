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

        // Создаем GlobalKTable из топика с четным возрастом
        GlobalKTable<Long, Person> evenAgePersons = builder.globalTable(
                KafkaConfig07.INPUT_EVEN_AGE_TOPIC,
                Consumed.with(Serdes.Long(), new PersonSerde())
        );

        // Создаем KStream из топика с нечетным возрастом
        KStream<Long, Person> oddAgePersons = builder.stream(
                KafkaConfig07.INPUT_ODD_AGE_TOPIC,
                Consumed.with(Serdes.Long(), new PersonSerde())
        );

        // Объединяем данные из KStream и GlobalKTable
        oddAgePersons.leftJoin(evenAgePersons,
                (personId, oddAgePerson) -> personId,  // Функция извлечения ключа для соединения
                (oddAgePerson, evenAgePerson) -> {
                    String result = "Odd age person: " + personToString(oddAgePerson);
                    if (evenAgePerson != null) {
                        result += ", Matched even age person: " + personToString(evenAgePerson);
                    } else {
                        result += ", No matching even age person";
                    }
                    return result;
                }
        ).to(KafkaConfig07.OUTPUT_TOPIC, Produced.with(Serdes.Long(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder.build(), config);

        // Выводим топологию
        logger.info("Topology: \n" + streams.toString());

        streams.start();

        // Добавляем хук завершения
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static String personToString(Person person) {
        if (person == null) {
            return "null";
        }
        return String.format("Person(id=%d, firstName='%s', lastName='%s', age=%d)",
                person.getId(), person.getFirstName(), person.getLastName(), person.getAge());
    }
}
