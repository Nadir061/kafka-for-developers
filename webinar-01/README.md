# webinar-01: Introduction to Kafka
[![Java](https://img.shields.io/badge/Java-E43222??style=for-the-badge&logo=openjdk&logoColor=FFFFFF)](https://www.java.com/)
[![Kafka](https://img.shields.io/badge/Kafka-000000??style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-0E2B62??style=for-the-badge&logo=Docker&logoColor=FFFFFF)](https://www.docker.com/)

## Kafka cluster
```txt
1) Брокер #1 
Порт PLAINTEXT_HOST://localhost:9093
2) zookeeper
Порт ZOOKEEPER_CLIENT_PORT: 2181
3) Kafdrop
Порт http://localhost:9000/ 
```

## Apache Kafka Clients for Java
https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients

Apache Kafka Clients for Java является частью проекта Apache Kafka, и она выпускается вместе с основным проектом Kafka

Описание: Эта библиотека предоставляет Java API для создания Kafka Producer и Kafka Consumer. Она включает в себя множество функций для управления процессом отправки и получения сообщений, обработки ошибок и управления конфигурациями.  

Подключение в проект:  

Maven (pom.xml) 
```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.1.0</version> <!-- Замените версию на актуальную -->
</dependency>
```

Gradle (build.gradle)  
```groovy
dependencies {
    implementation 'org.apache.kafka:kafka-clients:3.1.0' // Замените версию на актуальную
}
```

## Features list
```txt
Introduction to Kafka
---------------------
1) Подготовка: настройка Kafka, Zookeeper, Kafdrop  
  - Кластер Kafka
2) Сообщения и топики
  - Сообщение в Kафка
  - Тело сообщения: JSON, XML, Protobuf, Thrift, Avro
  - Топик  
  - Offset 
3) Партиции
  - Consumer group
  - Group coordinator
  - Group leader
  - Key
  - Привязка Консюмеров и Партиций
  - Offset и commit offset, топик топик "__consumer_offset"
  - rebalance
4) Репликация
  - Replication factor
5) Гарантии 
  - asks
  - committed messages и committed offsets
  - auto_commit
  - pull-модель и push-модель в брокерах сообщений
6) API
  - kafka-clients
    - Producer API
    - Consumer API
    - Admin API
  - kafka-streams: Stream API
  - Connect API   
```

## Demo's description
```txt
webinar-01
├── consumer-service
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.prosoft
│   │   │   │       └── config
│   │   │   │           ├── KafkaConfig.java
│   │   │   │           └── KafkaConsumerApp.java
│   │   │   ├── resources
│   │   │   │   └── logback.xml
│   │   ├── test
│   │       └── build.gradle.kts
│
├── producer-service
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.prosoft
│   │   │   │       └── config
│   │   │   │           ├── KafkaConfig.java
│   │   │   │           ├── KafkaProducer01App.java
│   │   │   │           ├── KafkaProducer02App.java
│   │   │   │           └── KafkaProducer03App.java
│   │   │   ├── resources
│   │   │   │   └── logback.xml
│   │   ├── test
│   │       └── build.gradle.kts
│
├── actions.md
├── docker-compose.yaml
└── README.md
```