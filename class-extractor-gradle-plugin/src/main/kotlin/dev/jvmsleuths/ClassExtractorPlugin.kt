package dev.jvmsleuths

import io.github.classgraph.ClassGraph
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class ExportClassesTask : DefaultTask() {

    @get:Classpath
    abstract val runtimeClasspath: ConfigurableFileCollection

    @get:Classpath
    abstract val compiledClasses: ConfigurableFileCollection

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun run() {

        val allClasspathFiles = runtimeClasspath.files + compiledClasses.files

        val scanResult = ClassGraph()
            .overrideClasspath(allClasspathFiles)
            .enableClassInfo()
            .scan()

        val classNames = scanResult.allClasses.map { it.name }.sorted()

        val reportFile = outputFile.asFile.get()
        reportFile.parentFile.mkdirs()
        reportFile.writeText(classNames.joinToString("\n"))
    }
}

class ClassExtractorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("generateClassReport", ExportClassesTask::class.java) {
            group = "jvmsleuths"
            description = "Create myfile.txt in the current directory"

            // Configure inputs and outputs during configuration time
            runtimeClasspath.from(project.configurations.getByName("runtimeClasspath"))

            compiledClasses.from(
                project.layout.buildDirectory.dir("classes/java/main"),
                project.layout.buildDirectory.dir("classes/kotlin/main")
            )

            outputFile.set(project.layout.buildDirectory.file("reports/classpath-report.txt"))

            // Ensure this task runs after compilation
            dependsOn("compileJava")
        }
    }
}