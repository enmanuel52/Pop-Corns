plugins {
    alias(libs.plugins.corntime.android.library)
    alias(libs.plugins.corntime.android.compose)
    alias(libs.plugins.corntime.android.test.coverage)
}


android {
    namespace = "com.enmanuelbergling.feature.watchlists"

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

    //adaptive navigation suite (bottom bar / nav rail)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)

    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.paging.compose)

    testImplementation(project(":core:testing"))
    testImplementation(libs.androidx.paging.testing)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.espresso.core)
}