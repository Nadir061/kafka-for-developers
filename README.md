[![Java](https://img.shields.io/badge/Java-E43222??style=for-the-badge&logo=openjdk&logoColor=FFFFFF)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-FFFFFF??style=for-the-badge&logo=Spring)](https://spring.io/projects/spring-boot/)
[![Kafka](https://img.shields.io/badge/Kafka-000000??style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-0E2B62??style=for-the-badge&logo=Docker&logoColor=FFFFFF)](https://www.docker.com/)
# kafka-for-developers
Training Course «Apache Kafka for Developers»

<img src="https://i.postimg.cc/3xsw3Jdh/kafka-cover.png" alt="Training Course «Apache Kafka for Developers»" />

### Project structure
```
kafka-for-developer/
├──...
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```


### Docker

1. [Zookeeper](https://zookeeper.apache.org/)  
   Docker image: confluentinc/cp-zookeeper:7.6.0  
   Docker Hub: https://hub.docker.com/r/confluentinc/cp-zookeeper  
   Info: образ Docker, содержащий Apache Zookeeper, который поставляется вместе с платформой Confluent для работы с Apache Kafka.
   Версия образа 7.6.0 соответствует версии Confluent Platform, которая включает в себя Apache Zookeeper версии 3.6.2.


2. [Kafka](https://kafka.apache.org/)  
   Docker image: confluentinc/cp-kafka:7.6.0  
   Docker Hub: https://hub.docker.com/r/confluentinc/cp-kafka  
   Info: контейнер Docker, содержащий Apache Kafka, от компании Confluent. Версия образа 7.6.0 соответствует версии
   Apache Kafka, которая включена в этот образ.


3. [Kafdrop](https://github.com/obsidiandynamics/kafdrop)  
   Docker image: linuxforhealth/kafdrop:latest
   Docker Hub: https://hub.docker.com/r/linuxforhealth/kafdrop  
   Info: веб-интерфейс для управления и мониторинга Apache Kafka, который позволяет взаимодействовать с кластером Kafka.




### Monitoring


1. Веб-интерфейс для мониторинга и управления Apache Kafka (Kafdrop) http://localhost:9000/


