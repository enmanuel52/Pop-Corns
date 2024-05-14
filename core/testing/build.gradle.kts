plugins {
    alias(libs.plugins.corntime.jvm.library)
}

dependencies {

    implementation(project(":core:domain"))
    implementation(project(":core:common:util"))

    implementation(libs.androidx.core.ktx)

    // Koin testing tools
    api(libs.koin.test)
    // Needed JUnit version
    api( libs.koin.test.junit4)

    //Kotest
    api(libs.kotest.runner.junit5)
    api(libs.kotest.extensions.koin)

    api(libs.kotlinx.coroutines.test)

    api(libs.junit)
}