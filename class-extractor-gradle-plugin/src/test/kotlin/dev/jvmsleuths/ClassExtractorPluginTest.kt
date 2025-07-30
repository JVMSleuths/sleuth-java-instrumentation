import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClassExtractorPluginTest {

    @TempDir
    lateinit var testProjectDir: File

    private lateinit var buildFile: File

    @BeforeEach
    fun setup() {
        buildFile = File(testProjectDir, "build.gradle.kts")
    }

    @Test
    fun `plugin applies successfully`() {
        buildFile.writeText("""
            plugins {
                java
                id("dev.jvmsleuths.class-extractor")
            }
            
            repositories {
                mavenCentral()
            }
            
            dependencies {
                implementation("com.google.guava:guava:32.1.3-jre")
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("tasks", "--all")
            .withPluginClasspath()
            .build()

        assertTrue(result.output.contains("generateClassReport"))
    }

    @Test
    fun `task executes and creates report file`() {
        // Create build file
        buildFile.writeText("""
            plugins {
                java
                id("dev.jvmsleuths.class-extractor")
            }
            
            repositories {
                mavenCentral()
            }
            
            dependencies {
                implementation("com.google.guava:guava:32.1.3-jre")
            }
        """.trimIndent())


        val srcDir = File(testProjectDir, "src/main/java/dev/jvmsleuths")
        srcDir.mkdirs()

        File(srcDir, "TestClass.java").writeText("""
            package dev.jvmsleuths;
            
            public class TestClass {
                public String hello() {
                    return "Hello World";
                }
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("generateClassReport", "--info")
            .withPluginClasspath()
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":generateClassReport")?.outcome)


        val reportFile = File(testProjectDir, "build/reports/classpath-report.txt")
        assertTrue(reportFile.exists(), "Report file should exist")

        val content = reportFile.readText()
        assertTrue(content.contains("dev.jvmsleuths.TestClass"), "Should contain our test class")
        assertTrue(content.contains("com.google.common"), "Should contain Guava classes")
    }

    @Test
    fun `task handles empty project`() {
        buildFile.writeText("""
            plugins {
                java
                id("dev.jvmsleuths.class-extractor")
            }
            
            repositories {
                mavenCentral()
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("generateClassReport")
            .withPluginClasspath()
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":generateClassReport")?.outcome)

        val reportFile = File(testProjectDir, "build/reports/classpath-report.txt")
        assertTrue(reportFile.exists())

        assertTrue(reportFile.readText().isEmpty())
    }

    @Test
    fun `task is up-to-date when nothing changes`() {
        buildFile.writeText("""
            plugins {
                java
                id("dev.jvmsleuths.class-extractor")
            }
            
            repositories {
                mavenCentral()
            }
        """.trimIndent())

        // Run twice
        GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("generateClassReport")
            .withPluginClasspath()
            .build()

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("generateClassReport")
            .withPluginClasspath()
            .build()

        assertEquals(TaskOutcome.UP_TO_DATE, result.task(":generateClassReport")?.outcome)
    }

    @Test
    fun `task detects changes in source code`() {
        buildFile.writeText("""
            plugins {
                java
                id("dev.jvmsleuths.class-extractor")
            }
            
            repositories {
                mavenCentral()
            }
        """.trimIndent())

        val srcDir = File(testProjectDir, "src/main/java/dev/jvmsleuths")
        srcDir.mkdirs()

        val javaFile = File(srcDir, "TestClass.java")
        javaFile.writeText("""
            package dev.jvmsleuths;
            public class TestClass {
                public String hello() { return "Hello"; }
            }
        """.trimIndent())

        // First run
        GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("generateClassReport")
            .withPluginClasspath()
            .build()

        // Modify the source file
        javaFile.writeText("""
            package dev.jvmsleuths;
            public class TestClass {
                public String hello() { return "Hello World"; }
                public String goodbye() { return "Goodbye"; }
            }
        """.trimIndent())

        // Second run should not be up-to-date
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("generateClassReport")
            .withPluginClasspath()
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":generateClassReport")?.outcome)
    }

    @Test
    fun `plugin works with configuration cache`() {
        buildFile.writeText("""
            plugins {
                java
                id("dev.jvmsleuths.class-extractor")
            }
            
            repositories {
                mavenCentral()
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("generateClassReport", "--configuration-cache")
            .withPluginClasspath()
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":generateClassReport")?.outcome)
        assertTrue(result.output.contains("Configuration cache entry stored"))
    }
}