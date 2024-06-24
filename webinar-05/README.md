# webinar-05 Kafka Transactions
### Kafka cluster
```txt
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT,PLAINTEXT_HOST2:PLAINTEXT
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:9093,PLAINTEXT_HOST2://localhost:9094

Kafka с одним брокером и двумя портами: 
  - порт для приема сообщений:   PLAINTEXT_HOST://localhost:9093
  - порт для отправки сообщений: PLAINTEXT_HOST2://localhost:9094  
```
### ConsumerConfig

```java
```

### Features list
```txt
1) Гарантии в Kafka
   - At Most Once
   - At Least Once
   - Exactly Once
2) Idempotent Producer
  - режим enable.idempotence=true
  
3) Exactly Once
  - параметр TRANSACTIONAL_ID_CONFIG
  - .initTransactions()
  - .beginTransaction()
  - .commitTransaction()
  - .abortTransaction()
  - служебный топик __transaction_state


```