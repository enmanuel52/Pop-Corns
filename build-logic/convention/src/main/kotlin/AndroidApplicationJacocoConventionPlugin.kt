import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import util.configureJacoco

class AndroidApplicationJacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Brings the JacocoReport task type and the "jacoco" extension onto the build.
            pluginManager.apply("jacoco")

            // ApplicationExtension is the DSL (buildTypes); ApplicationAndroidComponentsExtension
            // is the variant API. configureJacoco needs both.
            configureJacoco(
                extensions.getByType<ApplicationExtension>(),
                extensions.getByType<ApplicationAndroidComponentsExtension>(),
            )
        }
    }
}
