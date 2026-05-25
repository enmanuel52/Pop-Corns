package util

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    when (commonExtension) {
        is ApplicationExtension -> {
            commonExtension.compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()
            commonExtension.defaultConfig {
                minSdk = libs.findVersion("android-minSdk").get().toString().toInt()
                targetSdk = libs.findVersion("android-targetSdk").get().toString().toInt()
            }
            commonExtension.compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
        is LibraryExtension -> {
            commonExtension.compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()
            commonExtension.defaultConfig {
                minSdk = libs.findVersion("android-minSdk").get().toString().toInt()
            }
            commonExtension.compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }

    configureKotlin()
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    configureKotlin()
}

/**
 * Configure base Kotlin options
 */
private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}
