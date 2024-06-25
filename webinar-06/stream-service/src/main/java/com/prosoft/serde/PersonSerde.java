package com.prosoft.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prosoft.domain.Person;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.util.Map;

public class PersonSerde implements Serde<Person> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {
    }

    @Override
    public Serializer<Person> serializer() {
        return (topic, data) -> {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new RuntimeException("Error serializing Person to JSON", e);
            }
        };
    }

    @Override
    public Deserializer<Person> deserializer() {
        return (topic, data) -> {
            try {
                return objectMapper.readValue(data, Person.class);
            } catch (IOException e) {
                throw new RuntimeException("Error deserializing JSON to Person", e);
            }
        };
    }
}
