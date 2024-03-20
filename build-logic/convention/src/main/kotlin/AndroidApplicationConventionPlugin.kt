import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.buildTypes
import util.configureKotlinAndroid
import util.flavors

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins()

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)

                buildTypes()

                flavors()
            }
        }
    }

    private fun Project.applyPlugins() = apply() {
        plugin("com.android.application")
        plugin("org.jetbrains.kotlin.android")
    }
}