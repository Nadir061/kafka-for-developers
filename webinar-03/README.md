# webinar-03
### Kafka cluster
```txt
Kafka с двумя брокерами (репликацией) для повышения отказоустойчивости и масштабируемости системы
-------------------------------------------------------------------------------------------------
1. Добавление второго Kafka брокера (kafka2):

  - Добавлен сервис kafka2, который является вторым брокером Kafka
  - Каждый брокер имеет свой уникальный KAFKA_BROKER_ID (1 для kafka1, 2 для kafka2)
  - KAFKA_ZOOKEEPER_CONNECT указывает на тот же Zookeeper, что и для первого брокера

2. Настройка KAFKA_ADVERTISED_LISTENERS:

  Настройки позволяют клиентам подключаться к каждому брокеру через их уникальные адреса:
  
  - PLAINTEXT://kafka1:9092 и PLAINTEXT_HOST://localhost:9093 для kafka1
  - PLAINTEXT://kafka2:9092 и PLAINTEXT_HOST://localhost:9094 для kafka2

3. Изменения в kafdrop:
  
  - KAFKA_BROKERCONNECT теперь содержит адреса обоих брокеров через запятую (kafka1:9092, kafka2:9092)


                      Схема работы: 
                      ------------
                +---------------------+
                |     Zookeeper       |
                |   (Port: 2181)      |
                +---------|-----------+
                          |
          +-----------------------------+
          |                             |
          |                             |
          |                             |
+---------|-----------+       +---------|--------+
|    Kafka1           |       |    Kafka2        |
| (Broker ID: 1)      |       | (Broker ID: 2)   |
|  PLAINTEXT://       |       |  PLAINTEXT://    |
|  kafka1:9092        |       |  kafka2:9092     |
|  localhost:9093     |       |  localhost:9094  |
+---------|-----------+       +---------|--------+
          |                             |
          |                             |
          +-----------------------------+
            (Inter-Broker Communication)
                        |
                        |
                +-------|-------+
                |    Kafdrop    |
                | (Port: 9000)  |
                +---------------+

1. Zookeeper:

    Работает на порту 2181.
    Управляет метаданными и координацией для кластера Kafka.

2. Kafka1 и Kafka2:

    Два брокера Kafka с уникальными KAFKA_BROKER_ID (1 и 2).
    Оба брокера подключаются к Zookeeper для координации.
    Каждый брокер имеет два слушателя:
    Внутренний (PLAINTEXT://kafka1:9092, PLAINTEXT://kafka2:9092).
    Внешний (PLAINTEXT_HOST://localhost:9093, PLAINTEXT_HOST://localhost:9094).

3. Kafdrop:

    Сервис мониторинга, который подключается ко всем брокерам (kafka1:9092, kafka2:9092).
    Работает на порту 9000.
    
4. Пояснения:
    Zookeeper управляет метаданными и координацией между брокерами.
    Kafka1 и Kafka2 — это два брокера, которые взаимодействуют между собой для обеспечения репликации и отказоустойчивости.
    Kafdrop предоставляет интерфейс для мониторинга состояния кластера Kafka.
    В таком кластере, производители и потребители могут взаимодействовать с любым из брокеров (Kafka1 или Kafka2), 
    а Zookeeper обеспечивает надежную координацию и распределение нагрузки.
```
### ConsumerConfig

```java

```

### Features list
```txt
1) Настройка репликации Kafka, Zookeeper, Kafdrop 

2) partition.assignment.strategy — это параметр конфигурации в Apache Kafka, который определяет стратегию распределения партиций топика между консюмерами в группе консюмеров (Consumer Group)
      - RangeAssignor - 
      - RoundRobin - 
      - StickyAssignor - 
      - CooperativeStickyAssignor - 

3)  rebalance:
      - heartbeat.interval.ms - интервал отправки "сигналов жизни" (3 сек)
      - session.timeout.ms - время ожидания ответа на "сигналы жизни" (10 сек)
      - max.poll.interval.ms - время за которое Кансамер должен вызывать .poll() (5 мин) 
      - вызов rebalance:
           - .consumer.subscribe(Arrays.asList("topic1", "topic2")) - подписка на топик 
           - .close() - метод закрывает потребитель и освобождает все ресурсы
           - .unsubscribe() - отменяет подписку потребителя на все топики
           - .wakeup() - прерывает текущий вызов метода `poll()`, заставляя его немедленно выбросить `WakeupException`
           - .pause() - используется для временной приостановки получения данных из определённых партиций топика
      - ConsumerRebalanceListener - интерфейс, который позволяет определить, что делать в случае ребалансировки  
      - group.instance.id - статическое членство (static membership) группы позволяет консумерам сохранять свою принадлежность к группе даже при кратковременных отключениях   
4) Offset:
      - Auto-commit и топик "__consumer_offset"
      - enable.auto.commit=false - отключение Auto-commit (топик "__consumer_offset" не используется)
      - Consumer.commitSync() - блокирующий метод (синхронный), используется для ручной записи оффсета Консамером в топик "__consumer_offset"
      - Consumer.commitAsync() - неблокирующий метод (асинхронный), используется для ручной записи оффсета Консамером в топик "__consumer_offset". Возможно использовать Callback
      - Совместное использование: Consumer.commitAsync() - для основного чтения. Consumer.commitSync() - перед закрытием Consumer в (finally)
5) Consumer API configuration:
      - botstrap.servers - адрес брокера. "broker1:9093,broker2:9094"
      - key.deserializer - класс десерилизации ключа. Интерфейс org.apache.kafka.common.serialization.Deserializer 
      - кастомный десериализатор ключей. Интерфейс org.apache.kafka.common.serialization.Deserializer. Методы: configure, deserialize и close
      - value.deserializer - класс десериализации сообщения
      - кастомный десериализатор значений. Интерфейс org.apache.kafka.common.serialization.Deserializer. Методы: configure, deserialize 
      - group.id - идентификатор потребительской группы (consumer group)
      - enable.auto.commit. Параметр auto.commit.interval.ms (5 сек)
      - Consumer.poll(): 
            - max.partition.fetch.bytes - определяет максимальный размер данных, которые потребитель может запросить за один раз из каждой партиции 
            - max.poll.partitions - определяет максимальное количество разделов (partitions), которые Kafka-потребитель (consumer) может обрабатывать в рамках одного запроса на получение записей (poll request)
6) KafkaConsumer:
      - создание объекта Properties
      - создание объекта KafkaConsumer  
      - подписка на топики .subscribe() 
      - чтение сообщений: ConsumerRecords, метод .poll()
      - закрытие .close()
          
```