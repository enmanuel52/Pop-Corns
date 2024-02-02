import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
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
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation(platform("androidx.compose:compose-bom:2023.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //for collect
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")


    //Images--Icons
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.2")

    // Koin Multiplatform
    implementation("io.insert-koin:koin-compose:1.1.2")
    implementation("io.insert-koin:koin-core:3.5.0")

    implementation("androidx.core:core-splashscreen:1.0.1")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore:1.0.0")

    //ktor client
    val ktor_version = "2.3.5"
    implementation("io.ktor:ktor-client-core:$ktor_version")

    //Engines
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    //Plugins
    implementation("io.ktor:ktor-client-resources:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    //paging
    implementation("androidx.paging:paging-runtime:3.1.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha16")

    //Shimmer
    implementation("com.valentinilk.shimmer:compose-shimmer:1.0.5")

    //Rating Bar
    implementation("com.github.a914-gowtham:compose-ratingbar:1.3.4")

    val precompose_version = "1.5.8"

    implementation("moe.tlaster:precompose:$precompose_version")

    // For ViewModel intergration
    implementation("moe.tlaster:precompose-viewmodel:$precompose_version")
    // For Koin intergration
    implementation("moe.tlaster:precompose-koin:$precompose_version")
}