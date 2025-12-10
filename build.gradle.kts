plugins {
    java
    kotlin("jvm") version "2.0.0"
}

group = "test"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))

    // Rest Assured
    implementation("io.rest-assured:rest-assured:5.4.0")

    // Allure
    implementation("io.qameta.allure:allure-junit5:2.26.0")
    implementation("io.qameta.allure:allure-rest-assured:2.26.0")

    // Log4j2
    implementation("org.apache.logging.log4j:log4j-api:2.22.1")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.22.1")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    // curl-logger
    testImplementation("com.github.dzieciou.testing:curl-logger:3.0.0")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.13")

    // Hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")
}

tasks.test {
    useJUnitPlatform()
}
