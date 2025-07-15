plugins {
    `java`
}

group = "dev.jvmsleuths"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    testImplementation("org.testcontainers:testcontainers:1.19.0")
    testImplementation("org.testcontainers:junit-jupiter:1.19.0")


}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.11.3")
        }
    }
}


tasks.test {
    // make sure agent & test‑app jars exist before we run the tests
    dependsOn(":agent:shadowJar", ":test-app:bootJar")
}
