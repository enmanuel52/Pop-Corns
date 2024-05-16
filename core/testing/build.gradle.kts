plugins {
    alias(libs.plugins.corntime.jvm.library)
}

dependencies {

    implementation(project(":core:domain"))
    implementation(project(":core:common:util"))

    // Koin testing tools
    api(libs.koin.test)
    // Needed JUnit version
    api( libs.koin.test.junit4)

    //Kotest
    api(libs.kotest.runner.junit5)

    api(libs.kotlinx.coroutines.test)

    api(libs.junit)

    //Kotest
    testImplementation(libs.kotest.runner.junit5)

}