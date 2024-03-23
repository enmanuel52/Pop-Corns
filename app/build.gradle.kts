import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val secretFile = rootProject.file("secret.properties")
val secretProperties = Properties()
secretProperties.load(FileInputStream(secretFile))

android {
    namespace = "com.enmanuelbergling.ktormovies"
    compileSdk = 34

    defaultConfig {
        configurations.all {
            resolutionStrategy {
                force("androidx.emoji2:emoji2-views-helper:1.3.0")
                force("androidx.emoji2:emoji2:1.3.0")
                force("androidx.activity:activity-compose:1.7.2")
                exclude("androidx.fragment", "fragment")
            }
        }

        applicationId = "com.enmanuelbergling.ktormovies"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            type = "String",
            name = "API_KEY",
            value = "\"${secretProperties.getProperty("API_KEY")}\""
        )

        buildConfigField(
            type = "String",
            name = "ACCOUNT_ID",
            value = "\"${secretProperties.getProperty("ACCOUNT_ID")}\""
        )
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
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

    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.paging.compose)

    //Navigation
    implementation(libs.moe.tlaster.precompose)

    // For ViewModel intergration
    implementation(libs.moe.tlaster.precompose.viewmodel)
    // For Koin intergration
    implementation(libs.moe.tlaster.precompose.koin)
}