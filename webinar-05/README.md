# webinar-05: Kafka Transactions
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
4) schema-registry
Порт http://localhost:8081/

Возможны две конфигурации: (см. прим.)
1) Конфигурация: Kafka с одним брокером и двумя портами: 
--------------------------------------------------------
  - порт для приема сообщений:   PLAINTEXT_HOST://localhost:9093
  - порт для отправки сообщений: PLAINTEXT_HOST2://localhost:9094  

docker-compose.yaml
    environment:
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT,PLAINTEXT_HOST2:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:9093,PLAINTEXT_HOST2://localhost:9094
    ports:
      - "9092:9092"
      - "9093:9093"
      - "9094:9094"  

2) Конфигурация: Kafka с одним брокером и одним портом: 
-------------------------------------------------------
  - порт для приема и отправки сообщений: PLAINTEXT_HOST://localhost:9093

docker-compose.yaml
    environment:
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
    ports:
      - "9092:9092"
      - "9093:9093"
```

## Features list
```txt
1) Гарантии в Kafka
   - At Most Once
   - At Least Once
   - Exactly Once
2) Idempotent Producer
  - режим enable.idempotence=true
  - PID - идентификатор продюсера
  - Sequence Number - номер последовательности
  - max.in.flight.requests.per.connection
  - retries
3) Exactly Once
  - Транзакции
    - ACID в Kafka
    - параметр TRANSACTIONAL_ID_CONFIG
    - .initTransactions()
    - .beginTransaction()
    - .commitTransaction()
    - .abortTransaction()
    - Transaction Log и служебный топик __transaction_state
    - .sendOffsetsToTransaction()
    - номера Оффсетов при использовании транзакций в Kafka
    - Transaction Coordinator
    - transaction.id
  - Consumer isolation.level: read_uncommitted, read_committed 
  - Накладные расходы
    - transaction.timeout.ms - настройка длительности транзакций 
  - AdminClient
    - AbortTransactionResult
    - AbortTransactionSpec
    - ListTransactionsResult
    - TransactionListing
    - TransactionState: Ongoing, PrepareCommit, CompleteCommit, PrepareAbort, CompleteAbort, Empty. 
    - DescribeTransactionsResult
  - Реализация механизма защиты от дублирования при вхаимодействии Kafka с внешними системами.
```

## Demo's description
```txt
webinar-05
├── consumer-service
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.prosoft
│   │   │   │       ├── config
│   │   │   │       │   ├── KafkaConfig01
│   │   │   │       │   └── KafkaConfig02
│   │   │   │       ├── KafkaConsumer01App
│   │   │   │       └── KafkaConsumer02App
│   │   │   ├── resources
│   │   │   │   └── logback.xml
│   │   └── test
│   └── build.gradle.kts
├── producer-service
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.prosoft
│   │   │   │       ├── config
│   │   │   │       │   ├── KafkaConfig01
│   │   │   │       │   └── KafkaConfig02
│   │   │   │       ├── KafkaProducer01App
│   │   │   │       └── KafkaProducer02App
│   │   │   ├── resources
│   │   │   │   └── logback.xml
│   │   └── test
│   └── build.gradle.kts
├── build.gradle.kts
├── docker-compose.yaml
└── README.md
```
