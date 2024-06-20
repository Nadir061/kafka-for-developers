# webinar-02
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
### ProducerConfig
При использовании Kafka с несколькими брокерами, в конфигурации продюсера (ProducerConfig) нужно указать список всех 
брокеров, к которым продюсер может подключиться. Это делается для повышения отказоустойчивости: если один из брокеров 
недоступен, продюсер может отправить сообщения другому брокеру:  
```java
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093,localhost:9094");  
    // ...
```

### Features list
```txt
1) Настройка репликации Kafka, Zookeeper, Kafdrop 

2) Producer Record: 
   - Topic; 
   - Value (String); 
   - Байтовый массив (byte[]); 
   - Числовые типы (Integer, Long и т.д.); 
   - Пользовательские объекты (Custom Objects): - JSON (джейсон); 
   - установка размера ключа max.request.size;
   - Ключ (Message Key);
   - Partition: new ProducerRecord<>(topic, partition, key, value);
   - Timestamp: new ProducerRecord<>("my-topic", "key", "value", System.currentTimeMillis());
   - Headers: RecordHeaders headers = new RecordHeaders(), headers.add(new RecordHeader("header-key1", "header-value1".getBytes())), new ProducerRecord<>("my-topic", null, "key", "value", headers). 

3) Создание "пользовательского класса партицирования": implements Partitioner.  

4) Timestamp: CreateTime (Время создания), LogAppendTime (Время добавления в лог). 

5) Producer: 
  - compression.type: `none`, `gzip`, `snappy`, `lz4`;
  - linger.ms.

6) Producer API configuration:
  - botstrap.servers - адрес брокера. "broker1:9093,broker2:9094";
  - key.serializer - класс серилизации ключа;
  - кастомный сериализатор ключей: интерфейс Serializer;
  - value.serializer - класс сериализации сообщения;
  - compression.type - метод сжатия данных: "none"` (без сжатия), `"gzip"` (используется алгоритм Gzip), `"snappy"` (используется алгоритм Snappy), `"lz4"` (используется алгоритм LZ4); 
  - acks - метод подтверждения доставки: `0` (нет подтверждений), `1` (подтверждение лидера), `all` (подтверждение от всех реплик);
  - delivery.timeout.ms - сколько времени может быть потрачено на отправку сообщения (или время возвращения результата от .send());
  - batch.size максимальный размер батча (по умолчанию: 16384 (16 КБ)), props.put("batch.size", 32768);
  - linger.ms - время ожидания батчем новых сообщений перед отправкой: props.put("linger.ms", 0);
  - max.block.ms - максимальное время блокировки;
  - delivery.timeout.ms - максимальное время ожидания подтверждения;
  - retry.backoff.time - время задержки перед повторной попыткой;
  - request.timeout.ms - это параметр конфигурации в Kafka Producer API, который определяет максимальное время ожидания ответа на запрос от брокера Kafka после отправки запроса.
  
7) KafkaProducer:
      - создание объекта Properties;
      - создание объекта KafkaProducer;  
      - отправка сообщения:
            - асинхронный .send();
            - обработка Callback;   
            - блокирующий producer.send(record).get();  
            - producer.flush() - сброса буферов и отправки всех накопленных сообщений.    
      - закрытие:
            - .close();
            - .close(Duration.ofSeconds(60)).
```