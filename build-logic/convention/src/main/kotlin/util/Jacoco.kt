package util

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.api.variant.SourceDirectories
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale

// Classes excluded from coverage metrics because they contain no hand-written,
// testable logic — including them only deflates the percentages with noise.
private val coverageExclusions = listOf(
    // Android-generated code
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    // Compose compiler-generated lambda holders
    "**/*ComposableSingletons*",
    // Koin DI modules — pure dependency wiring, nothing to assert on
    "**/di/**",
)

// Variant names come in lowercase ("debug"); task names need them capitalized
// ("testDebugUnitTest"). titlecase() is the locale-safe replacement for the
// deprecated String.capitalize().
private fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

/**
 * Configures a combined (unit + instrumented) JaCoCo coverage report per build variant.
 *
 * Rather than hardcoding paths to compiled classes, it reads them from the Android
 * Gradle Plugin variant API, so the setup keeps working across AGP versions and
 * across build types / flavors.
 */
internal fun Project.configureJacoco(
    commonExtension: CommonExtension,
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>,
) {
    // Tell AGP to instrument the debug variant's bytecode so the JVM records which
    // lines run during tests. Without this the .exec / .ec files are never written.
    commonExtension.buildTypes.named("debug") {
        enableAndroidTestCoverage = true
        enableUnitTestCoverage = true
    }

    // Pin the JaCoCo engine version from the version catalog so every module agrees
    // on the binary format of the execution data.
    configure<JacocoPluginExtension> {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }

    androidComponentsExtension.onVariants { variant ->
        val objectFactory = project.objects
        val buildDir = layout.buildDirectory.get().asFile

        // Lazy holders that the ScopedArtifacts API (below) fills in with the
        // variant's compiled output: jars (from dependencies / packaged code) and
        // raw class directories (this module's own code).
        val allJars: ListProperty<RegularFile> = objectFactory.listProperty(RegularFile::class.java)
        val allDirectories: ListProperty<Directory> =
            objectFactory.listProperty(Directory::class.java)

        val reportTask = tasks.register(
            "create${variant.name.capitalize()}CombinedCoverageReport",
            JacocoReport::class,
        ) {
            // Make the report self-contained: running it produces fresh unit-test data.
            dependsOn("test${variant.name.capitalize()}UnitTest")

            // The compiled classes JaCoCo maps execution data back onto, minus the noise.
            classDirectories.setFrom(
                allJars,
                allDirectories.map { dirs ->
                    dirs.map { dir ->
                        objectFactory.fileTree().setDir(dir).exclude(coverageExclusions)
                    }
                },
            )

            reports {
                // XML for CI tooling (Codecov, SonarQube); HTML for humans.
                xml.required.set(true)
                html.required.set(true)
            }

            // Source roots so the HTML report can show annotated original code.
            fun SourceDirectories.Flat?.toFilePaths(): Provider<List<String>> = this
                ?.all
                ?.map { directories -> directories.map { it.asFile.path } }
                ?: provider { emptyList() }
            sourceDirectories.setFrom(
                files(
                    variant.sources.java.toFilePaths(),
                    variant.sources.kotlin.toFilePaths(),
                ),
            )

            // The binary hit-data: .exec from local unit tests, .ec from instrumented
            // (device) tests. Combining both gives a single coverage number per variant.
            executionData.setFrom(
                project.fileTree("$buildDir/outputs/unit_test_code_coverage/${variant.name}UnitTest")
                    .matching { include("**/*.exec") },
                project.fileTree("$buildDir/outputs/code_coverage/${variant.name}AndroidTest")
                    .matching { include("**/*.ec") },
            )
        }

        // Hand AGP the report task and ask it to populate allJars / allDirectories
        // with this project's CLASSES artifact for the current variant.
        variant.artifacts.forScope(ScopedArtifacts.Scope.PROJECT)
            .use(reportTask)
            .toGet(
                ScopedArtifact.CLASSES,
                { _ -> allJars },
                { _ -> allDirectories },
            )
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            // Required so JaCoCo doesn't choke on Robolectric's runtime-generated
            // classes that have no source location.
            isIncludeNoLocationClasses = true
            // Avoid instrumenting JDK internals on Java 11+, which throws otherwise.
            excludes = listOf("jdk.internal.*")
        }
    }
}
