# webinar-04
### Kafka cluster
```txt
Kafka с тремя брокерами (репликацией) для повышения отказоустойчивости и масштабируемости системы
```
### ConsumerConfig

```java

```

### Features list
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