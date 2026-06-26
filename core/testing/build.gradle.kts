plugins {
    alias(libs.plugins.corntime.jvm.library)
}

dependencies {

    implementation(project(":core:domain"))
    implementation(project(":core:common:util"))

    // Koin testing tools
    api(libs.koin.test)
    // Needed JUnit version
    api(libs.koin.test.junit4)
    api(libs.koin.test.junit5)

    api(libs.kotlinx.coroutines.test)

    api(libs.junit)
    api(libs.junit.jupiter.api)
    api(libs.junit.jupiter.engine)
    api(libs.junit.platform.launcher)

    api(libs.assertk)

}