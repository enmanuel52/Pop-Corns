import org.gradle.kotlin.dsl.androidTestImplementation
import util.libs as libsUtil

plugins {
    alias(libs.plugins.corntime.android.application)
    alias(libs.plugins.corntime.android.compose)
    alias(libs.plugins.corntime.android.application.jacoco)
}

android {
    namespace = "com.enmanuelbergling.ktormovies"

    defaultConfig {

        applicationId = "com.enmanuelbergling.ktormovies"
        targetSdk = libsUtil.findVersion("android-targetSdk").get().toString().toInt()
        versionCode = 1
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    lint {
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(project(":core:common:android-util"))
    implementation(project(":core:common:util"))
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":feature:actor"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:movies"))
    implementation(project(":feature:tvshows"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:watchlists"))
    implementation(project(":feature:favorites"))

    testImplementation(project(":core:testing"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.espresso.core)

    implementation(libs.androidx.core.splashscreen)

//    implementation(libs.walkthrough)
}