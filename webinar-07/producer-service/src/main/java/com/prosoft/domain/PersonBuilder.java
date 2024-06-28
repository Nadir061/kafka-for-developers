package com.prosoft.domain;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Служебный класс для создания записей Avro, представляющих объект Person.
 */
public class PersonBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PersonBuilder.class);

    private static final String PERSON_AVCS = String.valueOf(Paths.get("src", "main", "avro", "Person.avsc").toAbsolutePath());

    private PersonBuilder() {
    }

    public static void main(String[] args) {
        getPersonFromGenericRecord(1L, "John", "Doe", 32);
        doReflectDatum(1L, "John", "Doe", 32);
        doSpecificRecord(1L, "John", "Doe", 32);
    }

    /**
     * GenericRecord позволяет работать с Avro без необходимости компилировать схему в Java классы.
     * @param id
     * @param firstName
     * @param lastName
     * @param age
     * @return
     */
    private static GenericRecord getPersonFromGenericRecord(long id, String firstName, String lastName, int age) {

        /** Загрузка схемы из файла */
        Schema schema = null;
        try {
            schema = new Parser().parse(new File(PERSON_AVCS));
        } catch (IOException e) {
            logger.error("Ошибка при загрузке схемы Avro", e);
        }

        /** Создание экземпляра GenericRecord */
        GenericRecord person = new GenericData.Record(schema);
        person.put("id", id);
        person.put("firstName", firstName);
        person.put("lastName", lastName);
        person.put("age", age);
        logger.info("Создан GenericRecord для Person: {}", person);
        return person;
    }

    /***
     * Метод doReflectDatum позволяет работать с Avro, используя классы POJO (Plain Old Java Object), при этом
     * не требуется генерировать код из Avro схем.
     * @param id
     * @param firstName
     * @param lastName
     * @param age
     */
    public static void doReflectDatum(long id, String firstName, String lastName, int age) {

        /** Создание экземпляра класса Person */
        Person person = new Person(id, firstName, lastName, age);

        /** Получение схемы */
        Schema schema = ReflectData.get().getSchema(Person.class);

        /** Запись объекта в файл */
        File file = new File("person-reflect.avro");
        ReflectDatumWriter<Person> writer = new ReflectDatumWriter<>(schema);
        try (DataFileWriter<Person> dataFileWriter = new DataFileWriter<>(writer)) {
            dataFileWriter.create(schema, file);
            dataFileWriter.append(person);
        } catch (IOException e) {
            logger.error("Ошибка при записи объекта в файл Avro", e);
        }
        logger.info("ReflectDatum запись завершена.");
    }

    /**
     * Метод Specific (самый распространенный) использует автоматически сгенерированные классы из схемы Person.avsc
     * Класс генерируется с помощью команды mvn avro:schema или mvn package. Результат находится в папке target
     * и доступен в classpath.
     * @return
     */
    public static Person doSpecificRecord(long id, String firstName, String lastName, int age) {
        /** Создание объекта Person (impl. SpecificRecord) */
        Person person = Person.newBuilder()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAge(age).build();
        logger.info("Создан объект Person с использованием SpecificRecord: {}", person);
        return person;
    }



}
