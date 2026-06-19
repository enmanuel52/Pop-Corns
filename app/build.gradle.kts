import util.libs

plugins {
    alias(libs.plugins.corntime.android.application)
    alias(libs.plugins.corntime.android.compose)
}

android {
    namespace = "com.enmanuelbergling.ktormovies"

    defaultConfig {

        applicationId = "com.enmanuelbergling.ktormovies"
        targetSdk = libs.findVersion("android-targetSdk").get().toString().toInt()
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
    implementation(project(":feature:series"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:watchlists"))
    implementation(project(":feature:favorites"))

    testImplementation(project(":core:testing"))

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    implementation("androidx.core:core-splashscreen:1.2.0")

//    implementation(libs.walkthrough)
}