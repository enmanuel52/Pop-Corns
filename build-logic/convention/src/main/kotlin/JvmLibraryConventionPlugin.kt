import org.gradle.api.Plugin
import org.gradle.api.Project
import util.configureKotlinJvm

class JvmLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            applyPlugins()

            configureKotlinJvm()
        }
    }

    private fun Project.applyPlugins() = apply {
        plugin("java-library")
        plugin("org.jetbrains.kotlin.jvm")
    }
}