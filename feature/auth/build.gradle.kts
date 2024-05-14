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

    testImplementation(project(":core:testing"))

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.espresso.core)
}