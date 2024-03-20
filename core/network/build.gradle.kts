import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.corntime.android.library)

    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    alias(libs.plugins.de.jensklingenberg.ktorfit)
}

val secretFile = rootProject.file("secret.properties")
val secretProperties = Properties()
secretProperties.load(FileInputStream(secretFile))

android {
    namespace = "com.enmanuelbergling.core.network"

    defaultConfig{
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
}

dependencies {

    implementation(project(":core:domain"))
    implementation(project(":core:common:android-util"))
    implementation(project(":core:common:util"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.org.jetbrains.kotlinx.coroutines)

    //Koin
    implementation(libs.koin.core)

    //ktor client
    implementation(libs.io.ktor.ktor.client.core)

    //Engines
    implementation(libs.io.ktor.ktor.client.cio)

    //Plugins
    implementation(libs.io.ktor.ktor.client.resources)
    implementation(libs.io.ktor.ktor.client.websockets)
    implementation(libs.io.ktor.ktor.client.logging)
    implementation(libs.io.ktor.ktor.client.content.negotiation)
    implementation(libs.io.ktor.ktor.serialization.kotlinx.json)

    implementation(libs.kotlinx.serialization.json)

    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.common.ktx)

    //Ktorfit
    implementation(libs.de.jensklingenberg.ktorfit.ktorfit.lib.light)
    ksp(libs.de.jensklingenberg.ktorfit.ktorfit.ksp)
}