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
    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.22")
}

gradlePlugin {
    plugins {
        create("classExtractor") {
            id = "dev.jvmsleuths.class-extractor"
            implementationClass = "dev.jvmsleuths.ClassExtractorPlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

// Optional: Enable publishing to local repository for testing
publishing {
    repositories {
        mavenLocal()
    }
}