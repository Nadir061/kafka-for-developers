rootProject.name = "kafka-for-developers"

include("webinar-01")
include("webinar-01:producer-service")
include("webinar-01:consumer-service")
findProject(":webinar-01:producer-service")?.name = "producer-service"
findProject(":webinar-01:consumer-service")?.name = "consumer-service"

include("webinar-02")
include("webinar-02:producer-service")
include("webinar-02:consumer-service")
findProject(":webinar-02:producer-service")?.name = "producer-service"
findProject(":webinar-02:consumer-service")?.name = "consumer-service"
