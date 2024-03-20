plugins {
    alias(libs.plugins.corntime.android.library)
    alias(libs.plugins.corntime.android.compose)
}

android {
    namespace = "com.enmanuelbergling.core.ui"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common:util"))

    api(libs.androidx.activity.activity.compose)

    val composeBom = platform(libs.androidx.compose.bom)
    api(composeBom)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)

    api(libs.androidx.compose.foundation)

    androidTestApi(composeBom)
    androidTestApi(libs.androidx.compose.ui.test.junit4)
    debugApi(libs.androidx.compose.ui.tooling)
    debugApi(libs.androidx.compose.ui.test.manifest)

    //for collect
    api(libs.androidx.lifecycle.lifecycle.runtime.compose)
    
    //Images--Icons
    api(libs.io.coil.kt.coil.compose)
    api(libs.androidx.compose.material.icons.extended)

    // Koin Multiplatform
    api(libs.io.insert.koin.koin.compose)

    //Shimmer
    api(libs.com.valentinilk.shimmer.compose.shimmer)

    //Rating Bar
    api(libs.com.github.a914.gowtham.compose.ratingbar)

    implementation(libs.androidx.paging.paging.compose)
}