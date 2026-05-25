package util

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension,
) {
    when (commonExtension) {
        is ApplicationExtension -> {
            commonExtension.buildFeatures {
                compose = true
            }
        }
        is LibraryExtension -> {
            commonExtension.buildFeatures {
                compose = true
            }
        }
    }
}
