plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.testcontainers:testcontainers:1.19.1")
    testImplementation("org.testcontainers:postgresql:1.19.1")
    testImplementation ("org.postgresql:postgresql:42.2.27")
    implementation ("org.jooq:jooq:3.17.7")
    implementation ("org.jooq:jooq-meta:3.17.7")
    implementation ("org.jooq:jooq-codegen:3.17.7")
    implementation ("org.apache.logging.log4j:log4j-api:2.19.0")
    implementation ("org.apache.logging.log4j:log4j-core:2.19.0")
}

tasks.test {
    useJUnitPlatform()
}