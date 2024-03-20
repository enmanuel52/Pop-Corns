import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import util.configureAndroidCompose

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            runCatching {
                extensions.configure<LibraryExtension> {
                    configureAndroidCompose(this)
                }
            }.onFailure {
                println("Buy me library extension not found")
            }

            runCatching {
                extensions.configure<ApplicationExtension> {
                    configureAndroidCompose(this)
                }
            }.onFailure {
                println("Buy me application extension not found")
            }

        }
    }
}