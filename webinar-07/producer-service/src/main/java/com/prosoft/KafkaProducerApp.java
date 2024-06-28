package com.prosoft;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Webinar-07: Kafka producer-service (отправка объектов класса Person)
 */
public class KafkaProducerApp {

    public static void main(String[] args) {

        //1 - чтение схемы из файла student.avsc
        String personSchema = null;

        try {
            byte[] bytes = Files.readAllBytes(Paths.get("src", "main", "avro", "Person.avsc").toAbsolutePath());
            personSchema = new String(bytes);
        } catch (IOException e) {
            System.out.println(e);
        }

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(personSchema);

        //2 - Создание объекта Generic Record из схемы
        GenericRecord avroRecord = new GenericData.Record(schema);
        avroRecord.put("id", 1);
        avroRecord.put("firstName", "John");
        avroRecord.put("lastName", "Doe");
        avroRecord.put("age", 32);
        System.out.println(avroRecord);

    }

}