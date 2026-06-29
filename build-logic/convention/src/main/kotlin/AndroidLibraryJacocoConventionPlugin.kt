import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import util.configureJacoco

class AndroidLibraryJacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Brings the JacocoReport task type and the "jacoco" extension onto the build.
            pluginManager.apply("jacoco")

            // LibraryExtension is the DSL (buildTypes); LibraryAndroidComponentsExtension
            // is the variant API. configureJacoco needs both.
            configureJacoco(
                extensions.getByType<LibraryExtension>(),
                extensions.getByType<LibraryAndroidComponentsExtension>(),
            )
        }
    }
}
