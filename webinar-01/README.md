# webinar-01
# Apache Kafka Clients for Java
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

### Features list
```txt
1) Настройка Kafka, Zookeeper, Kafdrop 
```