# webinar-04: Admin API
[![Java](https://img.shields.io/badge/Java-E43222??style=for-the-badge&logo=openjdk&logoColor=FFFFFF)](https://www.java.com/)
[![Kafka](https://img.shields.io/badge/Kafka-000000??style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-0E2B62??style=for-the-badge&logo=Docker&logoColor=FFFFFF)](https://www.docker.com/)

## Kafka cluster
```txt
Kafka с тремя брокерами (репликацией) для повышения отказоустойчивости и масштабируемости системы
Порты PLAINTEXT_HOST://localhost:9091
      PLAINTEXT_HOST://localhost:9092
      PLAINTEXT_HOST://localhost:9093
```

## Features list
```txt

1) Устройство Admin API
   - ?
   ...
2) Управление топиками:
   - Создание топика:
      - Класс NewTopic
      - Класс TopicConfig
   - Описание топика: 
      - Класс DescribeTopicsResult
      - Класс TopicDescription 
      - Класс ListTopicsResult
      - Класс ListTopicsOptions
      - Класс TopicPartitionInfo
   - Удаление топика:
      - Класс DeleteTopicsResult   
   - Добавление партиций:
      - Класс CreatePartitionsResult
      - Класс NewPartitions   
   + Перемещение партиций: 
      + Класс AlterPartitionReassignmentsResult   
      + Класс NewPartitionReassignment
      + Класс ListPartitionReassignmentsResult
      + Класс PartitionReassignment
      - (to-do добавить демо)
   + Хранение в файловой системе
      + Класс DescribeLogDirsResult
      + Класс LogDirDescription
      + Класс DescribeReplicaLogDirsResult
      + Класс ReplicaLogDirInfo
      + Класс AlterReplicaLogDirsResult
      - (to-do добавить демо)
   - Управление транзакциями
      - Класс DescribeTransactionsResult
      - Класс AbortTransactionResult
      - Класс ListTransactionsResult
      - (to-do добавить демо)   
3) Сообщения и консамеры:
   - Удаление записей:
      - Класс DeleteRecordsResult
      - Класс RecordsToDelete
      - (to-do добавить демо)
   - Consumer group:
      - Класс ListConsumerGroupsResult
      - Класс DescribeConsumerGroupsResult
      - Класс DeleteConsumerGroupsResult
      - Класс ListConsumerGroupOffsetsResult
      - Класс ListConsumerGroupOffsetsSpec
      - Класс DeleteConsumerGroupOffsetsResult
      - Класс AlterConsumerGroupOffsetsResult
      - (to-do добавить демо)
4) Авторизация:
   - Управление ACL:
      - Класс CreateAclsResult
      - Класс AclBinding
      - Класс ResourcePattern
      - Класс AccessControlEntry
      - Класс DeleteAclsResult
      - Класс DescribeAclsResult
      - Класс AclBindingFilter
      - Класс ResourcePatternFilter
      - Класс AccessControlEntryFilter
      - (to-do добавить демо)
   - Управление квотами:
      - Класс AlterClientQuotasResult
      - Класс ClientQuotaAlteration
      - Класс ClientQuotaEntity
      - Класс Op
      - Класс DescribeClientQuotasResult
      - Класс ClientQuotaFilter
      - Класс ClientQuotaFilterComponent
      - (to-do добавить демо)   
5) Прочее:
   - Настройка топиков и брокеров:
      - Класс AlterConfigsResult
      - Класс ConfigResource
      - Класс ConfigEntry
      - Класс DescribeConfigsResult
      - (to-do добавить демо)
   - Описание кластера:
      - Класс DescribeClusterResult
      - Класс DescribeClusterResult
      - (to-do добавить демо)
```

## Demo's description
```txt
webinar-04
├── admin-service
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.prosoft
│   │   │   │       └── config
│   │   │   │           ├── KafkaConfig
│   │   │   │           ├── CreateTopics
│   │   │   │           └── KafkaAdminApp
│   │   │   ├── resources
│   │   │   │   └── logback.xml
│   ├── test
│   │   └── build.gradle.kts
│
├── actions.md
├── build.gradle.kts
├── docker-compose.yaml
└── README.md
```

### TO-DO list
```
  1) пример Перемещания партиций: AlterPartitionReassignmentsResult, NewPartitionReassignment, ListPartitionReassignmentsResult, PartitionReassignment;

  2) пример Хранение в файловой системе: DescribeLogDirsResult, LogDirDescription, DescribeReplicaLogDirsResult, ReplicaLogDirInfo, AlterReplicaLogDirsResult;

  3) пример Управление транзакциями: DescribeTransactionsResult, AbortTransactionResult, ListTransactionsResult;

  4) пример Удаление записей: DeleteRecordsResult, RecordsToDelete;

  5) пример Consumer group: ListConsumerGroupsResult, DescribeConsumerGroupsResult, DeleteConsumerGroupsResult, ListConsumerGroupOffsetsResult, ListConsumerGroupOffsetsSpec, DeleteConsumerGroupOffsetsResult, AlterConsumerGroupOffsetsResult;

  6) пример Управление ACL: CreateAclsResult, AclBinding, ResourcePattern, AccessControlEntry, DeleteAclsResult, DescribeAclsResult, AclBindingFilter, ResourcePatternFilter, AccessControlEntryFilter;

  7) пример Управление квотами: AlterClientQuotasResult, ClientQuotaAlteration, ClientQuotaEntity, Op, DescribeClientQuotasResult, ClientQuotaFilter, ClientQuotaFilterComponent;

  8) пример Настройка топиков и брокеров: AlterConfigsResult, ConfigResource, ConfigEntry, DescribeConfigsResult;

  9) пример Описание кластера: DescribeClusterResult, DescribeClusterResult.

```
