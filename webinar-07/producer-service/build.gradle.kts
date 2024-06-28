plugins {
    id("java")
}

group = "com.prosoft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    implementation("org.apache.avro:avro:1.10.2")
    implementation("org.apache.kafka:kafka-clients:3.5.0")
    implementation("io.confluent:kafka-avro-serializer:7.4.0")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

tasks.register<Exec>("generateAvroJava") {
    group = "build"
    description = "Generate Java sources from Avro schemas"
    commandLine("java", "-jar", "$rootDir/avro-tools-1.10.2.jar", "compile", "schema", "$rootDir/src/main/avro", "$rootDir/src/main/java")
    inputs.dir("$rootDir/src/main/avro")
    outputs.dir("$rootDir/src/main/java")
}

tasks.compileJava {
    dependsOn(tasks.named("generateAvroJava"))
}
