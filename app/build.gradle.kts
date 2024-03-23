
plugins {
   alias(libs.plugins.corntime.android.application)
   alias(libs.plugins.corntime.android.compose)
}

android {
    namespace = "com.enmanuelbergling.ktormovies"

    defaultConfig {

        applicationId = "com.enmanuelbergling.ktormovies"
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    implementation(project(":feature:watchlists"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.espresso.core)

    implementation(libs.koin.core)

    implementation(libs.androidx.core.splashscreen)

    //Navigation
    implementation(libs.moe.tlaster.precompose)

    // For ViewModel intergration
    implementation(libs.moe.tlaster.precompose.viewmodel)
    // For Koin intergration
    implementation(libs.moe.tlaster.precompose.koin)
}