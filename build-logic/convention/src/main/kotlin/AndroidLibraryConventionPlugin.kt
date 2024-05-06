import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.buildTypes
import util.configureKotlinAndroid
import util.defaultConfig

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins()

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig()

                buildTypes()
            }
        }
    }

    private fun Project.applyPlugins() = apply {
        plugin("com.android.library")
        plugin("org.jetbrains.kotlin.android")
        plugin("org.jetbrains.kotlin.plugin.serialization")
    }

}