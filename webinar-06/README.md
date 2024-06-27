# webinar-06: Kafka Streams
[![Java](https://img.shields.io/badge/Java-E43222??style=for-the-badge&logo=openjdk&logoColor=FFFFFF)](https://www.java.com/)
[![Kafka](https://img.shields.io/badge/Kafka-000000??style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-0E2B62??style=for-the-badge&logo=Docker&logoColor=FFFFFF)](https://www.docker.com/)

## Kafka cluster
```txt
Один брокер. Порт 9093.
```

## Features list
```txt
1) Библиотека kafka-streams
   - DAG
   - Streams API
   - APPLICATION_ID_CONFIG - уникальный идентификатор для приложения
   - DEFAULT_KEY_SERDE_CLASS_CONFIG, DEFAULT_VALUE_SERDE_CLASS_CONFIG - Классы Сериализации и Десериализации (Serde)
   - Методы интерфейса Serde: serializer(), deserializer(), configure(), close()
   - Класс Topology 
   - Класс StreamsBuilder
   - Класс KStream: методы map(), mapValues(), filter(), flatMap(), flatMapValues(), join(), groupBy(), aggregate(), to(), print();
   - Класс Printed: метод withLabel()
   - Класс KafkaStreams
2) Хранение состояния
   - Локальные хранилища состояний
   - Основные классы и интерфейсы: Stores, StoreBuilder, KeyValueStore, Processor, ProcessorContext
   - Методы для работы с хранилищем: putIfAbsent(), get(), put()
   - Резервирование: топик "-changelog" 
   - Создание хранилища состояний: 
     - методы inMemoryKeyValueStore(), persistentKeyValueStore(). 
     - Класс StoreBuilder
     - метод addStateStore()
   - Хранилища состояний: 
     - Методы Stores: persistentKeyValueStore, inMemoryKeyValueStore, lruMap
     - Использование базы данных RocksDB в реализации persistentKeyValueStore 
   - Журналирование:
     - параметры: retention.ms, cleanup.policy, segment.ms, segment.bytes, min.cleanable.dirty.ratio, delete.retention.ms, ax.message.bytes, ile.delete.delay.ms
     - методы: withLoggingDisabled(), withLoggingEnabled()   
3) Объединение данных
   - Join: join, leftJoin, outerJoin
   - интерфейс ValueJoiner
   - метод apply()
   - TimestampExtractor: ExtractRecordMetadataTimestamp, WallclockTimestampExtractor
   - Репартиционирование   
4) KTable
   - методы класса KTable: filter(), mapValues(), join(), groupBy(), toStream(), suppress(), queryableStoreName()
   - особенности использования 
5) GlobalKTable
   - методы globalTable(), join(), queryableStoreName()
6) Processor API
   - Класс Topology
   - методы: addSource(), addProcessor(), addSink()
7) Получение данных из локальных хранилищ состояний

```

## Demo's description
```txt
webinar-06
├── producer-service
│   ├── build
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com.prosoft
│       │   │       ├── config
│       │   │       │   └── KafkaConfig
│       │   │       ├── domain
│       │   │       │   └── Person
│       │   │       ├── serializer
│       │   │       │   └── PersonSerializer
│       │   │       └── KafkaProducerApp
│       │   └── resources
│       │       └── logback.xml
│       └── test
│           └── build.gradle.kts
├── stream-service
│   ├── build
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com.prosoft
│       │   │       ├── config
│       │   │       │   ├── KafkaConfig01
│       │   │       │   ├── KafkaConfig02
│       │   │       │   ├── KafkaConfig03
│       │   │       │   ├── KafkaConfig04
│       │   │       │   ├── KafkaConfig05
│       │   │       │   ├── KafkaConfig06
│       │   │       │   ├── KafkaConfig07
│       │   │       │   └── KafkaConfig08
│       │   │       ├── domain
│       │   │       │   └── Person
│       │   │       ├── serde
│       │   │       │   └── PersonSerde
│       │   │       ├── KafkaStream01App
│       │   │       ├── KafkaStream02App
│       │   │       ├── KafkaStream03App
│       │   │       ├── KafkaStream04App
│       │   │       ├── KafkaStream05App
│       │   │       ├── KafkaStream06App
│       │   │       ├── KafkaStream07App
│       │   │       └── KafkaStream08App
│       │   └── resources
│       │       └── logback.xml
│       └── test
│           └── build.gradle.kts
├── build.gradle.kts
├── docker-compose.yaml
└── README.md
```
