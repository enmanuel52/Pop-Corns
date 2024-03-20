plugins {
    alias(libs.plugins.corntime.jvm.library)

    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    alias(libs.plugins.de.jensklingenberg.ktorfit)
}

dependencies {
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

    //Ktorfit
    implementation(libs.de.jensklingenberg.ktorfit.ktorfit.lib.light)
    ksp(libs.de.jensklingenberg.ktorfit.ktorfit.ksp)

    //Koin
    implementation(libs.koin.core)
}