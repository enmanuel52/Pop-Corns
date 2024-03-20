package util

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        defaultConfig {
            configurations.all {
                resolutionStrategy {
                    force("androidx.emoji2:emoji2-views-helper:1.3.0")
                    force("androidx.emoji2:emoji2:1.3.0")
                }
            }

            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = "1.5.3"
            }
        }
    }
}