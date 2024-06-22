# webinar-03
### Kafka cluster
```txt
Kafka с тремя брокерами (репликацией) для повышения отказоустойчивости и масштабируемости системы
```
### ConsumerConfig

```java

```

### Features list
```txt
1) Настройка репликации Kafka, Zookeeper, Kafdrop 

2) partition.assignment.strategy — это параметр конфигурации в Apache Kafka, который определяет стратегию распределения партиций топика между консюмерами в группе консюмеров (Consumer Group)
      - RangeAssignor;
      - RoundRobin;
      - StickyAssignor; 
      - CooperativeStickyAssignor; 

3)  rebalance:
      - heartbeat.interval.ms - интервал отправки "сигналов жизни" (по умолч. 3 сек);
      - session.timeout.ms - время ожидания ответа на "сигналы жизни" (по умолч. 10 сек);
      - max.poll.interval.ms - время за которое Кансамер должен вызывать .poll() (по умолч. 5 мин); 
      - вызов rebalance:
           - .consumer.subscribe(Arrays.asList("topic1", "topic2")) - подписка на топик; 
           - .close() - метод закрывает потребитель и освобождает все ресурсы;
           - .unsubscribe() - отменяет подписку потребителя на все топики;
           - .wakeup() - прерывает текущий вызов метода `poll()`, заставляя его немедленно выбросить WakeupException;
           - .pause() - используется для временной приостановки получения данных из определённых партиций топика;
      - ConsumerRebalanceListener - интерфейс, который позволяет определить, что делать в случае ребалансировки;  
      - group.instance.id - статическое членство (static membership).   
4) Offset:
      - Auto-commit и топик "__consumer_offset";
      - enable.auto.commit=false - отключение Auto-commit (топик "__consumer_offset" не используется);
      - Consumer.commitSync() - блокирующий метод (синхронный), используется для ручной записи оффсета Консамером в топик "__consumer_offset";
      - Consumer.commitAsync() - неблокирующий метод (асинхронный), используется для ручной записи оффсета Консамером в топик "__consumer_offset". Возможно использовать Callback;
      - Совместное использование: Consumer.commitAsync() - для основного чтения. Consumer.commitSync() - перед закрытием Consumer в (finally);
5) Consumer API configuration:
      - botstrap.servers - адрес брокера. "broker1:9093,broker2:9094";
      - key.deserializer - класс десерилизации ключа. Интерфейс org.apache.kafka.common.serialization.Deserializer; 
      - кастомный десериализатор ключей. Интерфейс org.apache.kafka.common.serialization.Deserializer. Методы: configure, deserialize и close;
      - value.deserializer - класс десериализации сообщения;
      - кастомный десериализатор значений. Интерфейс org.apache.kafka.common.serialization.Deserializer. Методы: configure, deserialize; 
      - group.id - идентификатор потребительской группы (consumer group);
      - enable.auto.commit. Параметр auto.commit.interval.ms (по умолч. 5 сек);
      - Consumer.poll(): 
            - max.partition.fetch.bytes - определяет максимальный размер данных, которые потребитель может запросить за один раз из каждой партиции; 
            - max.poll.partitions - определяет максимальное количество разделов (partitions), которые Kafka-потребитель (consumer) может обрабатывать в рамках одного запроса на получение записей (poll request).
6) KafkaConsumer:
      - создание объекта Properties;
      - создание объекта KafkaConsumer;  
      - подписка на топики .subscribe(); 
      - чтение сообщений: ConsumerRecords, метод .poll();
      - закрытие .close().   
```