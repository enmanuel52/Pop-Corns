import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath(libs.kotlin.serialization)
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.com.android.library) apply false
    kotlin("plugin.serialization") version "1.9.22" apply false
    // jacoco is a Gradle core plugin — no external dependency or version catalog entry needed.
    id("jacoco")
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            // This assumes you might have other Kotlin targets (like JVM) and want to apply these flags there as well.
            // If this is a pure Android project, you might not strictly need this cast, but it's safer.
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs.add("-P")
                freeCompilerArgs.add("plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_compiler")
            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs.add("-P")
                freeCompilerArgs.add("plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_compiler")
            }
        }
    }
}

// Mirrors the exclusion list in AndroidTestCoverageConventionPlugin so the merged
// report skips the same noise as the per-module reports.
private val mergedReportExcludes = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*ComposableSingletons*",
    "**/di/**",
)

// Aggregates the per-module JaCoCo reports into a single merged HTML + XML report
// at build/reports/jacoco/merged/.
// Run with: ./gradlew jacocoMergedReport
tasks.register<JacocoReport>("jacocoMergedReport") {
    group = "verification"
    description = "Merges JaCoCo coverage data from all modules into one report."

    // Declare a task dependency on each module's jacocoTestReport so that
    // running this task alone is enough to produce fresh data for all modules.
    // tasks.matching {} is configuration-lazy — it won't fail if a subproject
    // doesn't have the task.
    dependsOn(subprojects.map { it.tasks.matching { t -> t.name == "jacocoTestReport" } })

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/merged/html"))
        xml.outputLocation.set(
            layout.buildDirectory.file("reports/jacoco/merged/jacocoMergedReport.xml")
        )
    }

    // Collect class files from every subproject's debug Kotlin compile output.
    // fileTree() is lazy: the directory is only walked when the task actually runs,
    // so it's fine if it doesn't exist at configuration time.
    classDirectories.setFrom(
        subprojects.map { sub ->
            sub.fileTree("${sub.buildDir}/tmp/kotlin-classes/debug") {
                exclude(mergedReportExcludes)
            }
        }
    )

    // All source roots so JaCoCo can show original code in the HTML report.
    sourceDirectories.setFrom(
        subprojects.flatMap { sub ->
            listOf("src/main/java", "src/main/kotlin").map { sub.file(it) }
        }
    )

    // Collect every .exec file produced by the per-module testDebugUnitTest tasks.
    // The glob **/*.exec picks them up regardless of variant naming differences.
    executionData.setFrom(
        subprojects.map { sub ->
            sub.fileTree("${sub.buildDir}/outputs/unit_test_code_coverage") {
                include("**/*.exec")
            }
        }
    )
}