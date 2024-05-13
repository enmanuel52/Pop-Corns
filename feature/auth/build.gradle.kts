plugins {
    alias(libs.plugins.corntime.android.library)
    alias(libs.plugins.corntime.android.compose)
}

android {
    namespace = "com.enmanuelbergling.feature.auth"

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":core:common:util"))
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.runtime.ktx)

    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.paging.compose)

    // Koin testing tools
    testImplementation(libs.koin.test)
    // Needed JUnit version
//    api( libs.koin.test.junit4)

    //Kotest
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.extensions.koin)

    testImplementation(platform(libs.junit.jupiter.bom))
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.kotlinx.coroutines.test)

    testRuntimeOnly(libs.junit.jupiter.engine)

    testFixturesImplementation(libs.kotest.runner.junit5)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.espresso.core)
}

//tasks.withType<Test>().configureEach {
//    useJUnitPlatform()
//}