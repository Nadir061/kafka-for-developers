rootProject.name = "kafka-for-developers"
include("webinar-01")
include("webinar-01:producer-service")
findProject(":webinar-01:producer-service")?.name = "producer-service"
