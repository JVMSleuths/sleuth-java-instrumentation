plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

// Ensure Kotlin compilation works correctly
kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("io.github.classgraph:classgraph:4.8.158")
}

gradlePlugin {
    plugins {
        create("classExtractor") {
            id = "dev.jvmsleuths.class-extractor"
            implementationClass = "dev.jvmsleuths.ClassExtractorPlugin"
        }
    }
}

// Optional: Enable publishing to local repository for testing
publishing {
    repositories {
        mavenLocal()
    }
}