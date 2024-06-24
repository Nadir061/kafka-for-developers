# webinar-05 Kafka Transactions
### Kafka cluster
```txt
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT,PLAINTEXT_HOST2:PLAINTEXT
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:9093,PLAINTEXT_HOST2://localhost:9094

Kafka с одним брокером и двумя портами: 
  - порт для приема сообщений:   PLAINTEXT_HOST://localhost:9093
  - порт для отправки сообщений: PLAINTEXT_HOST2://localhost:9094  
```

### Features list
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