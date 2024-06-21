# webinar-02
### Kafka cluster
```txt
Кластер Kafka с тремя брокерами (репликацией) для повышения отказоустойчивости и масштабируемости системы
---------------------------------------------------------------------------------------------------------
```
### ProducerConfig
При использовании Kafka с несколькими брокерами, в конфигурации продюсера (ProducerConfig) нужно указать список всех 
брокеров, к которым продюсер может подключиться. Это делается для повышения отказоустойчивости: если один из брокеров 
недоступен, продюсер может отправить сообщения другому брокеру:  
```java
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091, localhost:9092, localhost:9093");  
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
  - botstrap.servers - адрес брокера. "localhost:9091, localhost:9092, localhost:9093";
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
