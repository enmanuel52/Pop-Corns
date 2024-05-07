plugins {
    alias(libs.plugins.corntime.android.library)
    alias(libs.plugins.corntime.android.compose)
}


android {
    namespace = "com.enmanuelbergling.feature.series"
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

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}