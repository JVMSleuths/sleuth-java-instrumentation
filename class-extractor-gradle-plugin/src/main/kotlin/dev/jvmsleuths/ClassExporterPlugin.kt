package dev.jvmsleuths

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import java.io.File

abstract class ExportClassesToCsvTask : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val classDirs: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun run() {
        val outDir = outputDir.get().asFile.also { it.mkdirs() }
        val csv = File(outDir, "all_classes.csv")
        csv.writeText("class\n")

        classDirs.files
            .forEach { dir ->
                dir.walkTopDown()
                    .filter { it.isFile && it.extension == "class" }
                    .map { file -> file.relativeTo(dir).invariantSeparatorsPath.removeSuffix(".class").replace('/', '.') }
                    .distinct()
                    .sorted()
                    .forEach { name -> csv.appendText("$name\n") }
            }

        println("✔ Exported ${'$'}{csv.readLines().size - 1} classes to ${'$'}{csv.path}")
    }
}


class ClassExtractorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("createFileTaskFromBinaryPlugin", ExportClassesToCsvTask::class.java) {
            group = "from my binary plugin"
            description = "Create myfile.txt in the current directory"
        }
    }
}
