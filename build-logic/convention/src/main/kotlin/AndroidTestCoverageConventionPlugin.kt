import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

class AndroidTestCoverageConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(JacocoPlugin::class.java)

            extensions.configure<LibraryExtension> {
                buildTypes {
                    debug {
                        // Tells AGP to instrument the bytecode of the debug variant so that
                        // the JVM can record which lines are executed during unit tests.
                        // Without this, the .exec file is never written and the report is empty.
                        enableUnitTestCoverage = true
                    }
                }
            }

            tasks.register<JacocoReport>("jacocoTestReport") {
                // The test task must run first to produce the .exec file.
                dependsOn("testDebugUnitTest")
                group = "verification"
                description = "Generates JaCoCo coverage report for debug unit tests."

                reports {
                    // XML is consumed by CI tools (Codecov, SonarQube, GitHub Actions summary).
                    xml.required.set(true)
                    // HTML is the human-readable browser report.
                    html.required.set(true)
                }

                // The compiled .class files for the debug variant.
                // JaCoCo needs these to map execution data back to source lines.
                // Using a lazy Provider so the directory is resolved at execution time,
                // not configuration time (the directory doesn't exist before the build runs).
                val classDir = project.layout.buildDirectory.dir("tmp/kotlin-classes/debug")
                classDirectories.setFrom(
                    project.files(classDir).asFileTree.matching {
                        exclude(coverageExcludes)
                    }
                )

                // The Kotlin/Java source files JaCoCo uses to annotate the HTML report
                // with the original source code next to coverage markers.
                sourceDirectories.setFrom(
                    project.files("src/main/java", "src/main/kotlin")
                )

                // The binary execution data file written by the JaCoCo Java agent while
                // the tests run. AGP places it here when enableUnitTestCoverage = true.
                executionData.setFrom(
                    project.files(
                        project.layout.buildDirectory.file(
                            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
                        )
                    )
                )
            }
        }
    }
}

// Classes to exclude from coverage metrics:
//  • Android-generated files have no business logic and inflate noise.
//  • Compose-generated singletons are compiler artefacts, not hand-written code.
//  • DI modules wire dependencies — there is nothing meaningful to test there.
internal val coverageExcludes = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*ComposableSingletons*",
    "**/di/**",
)
