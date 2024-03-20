plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.activity.compose)

    val composeBom = platform(libs.androidx.compose.bom)
    api(composeBom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.compose.foundation)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.espresso.core)
    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //for collect
    implementation(libs.androidx.lifecycle.lifecycle.runtime.compose)


    //Images--Icons
    implementation(libs.io.coil.kt.coil.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    // Koin Multiplatform
    implementation(libs.io.insert.koin.koin.compose)
    implementation(libs.koin.core)

    implementation(libs.androidx.core.splashscreen)

    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.paging.compose)

    //Shimmer
    implementation(libs.com.valentinilk.shimmer.compose.shimmer)

    //Rating Bar
    implementation(libs.com.github.a914.gowtham.compose.ratingbar)

    implementation(libs.moe.tlaster.precompose)

    // For ViewModel intergration
    implementation(libs.moe.tlaster.precompose.viewmodel)
    // For Koin intergration
    implementation(libs.moe.tlaster.precompose.koin)
}