import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    java
    application
}

group = "io.jvmsleuths"
version = "0.0.1-SNAPSHOT"

application {
    mainClass.set("io.jvmsleuths.TestApp")
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// configure the existing bootJar task to drop the version suffix
tasks.named<BootJar>("bootJar") {
    archiveBaseName.set("myapp")
    archiveVersion.set("")    // yields exactly "myapp.jar"
}
