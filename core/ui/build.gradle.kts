plugins {
    alias(libs.plugins.corntime.android.library)
    alias(libs.plugins.corntime.android.compose)
}

android {
    namespace = "com.enmanuelbergling.core.ui"
}

dependencies {
    implementation(project(":core:model"))
    api(project(":core:common:android-util"))
    api(project(":core:common:util"))

    api(libs.androidx.activity.activity.compose)
    implementation(libs.androidx.ui.text.google.fonts)

    val composeBom = platform(libs.androidx.compose.bom)
    api(composeBom)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)

    api(libs.androidx.compose.foundation)

    api(libs.androidx.constraintlayout.compose)

    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //for collect
    api(libs.androidx.lifecycle.lifecycle.runtime.compose)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.lifecycle.viewmodel.ktx)

    //Images--Icons
    api(libs.io.coil.kt.coil.compose)
    api(libs.androidx.compose.material.icons.extended)

    // Koin Multiplatform soon
//    api(libs.io.insert.koin.compose)
    api(libs.koin.compose.androidx)

    //Shimmer
    api(libs.com.valentinilk.shimmer.compose.shimmer)

    api(libs.orbital)
    api(libs.haze)
    api(libs.haze.blur)
    api(libs.liquid)

    //shared element transition
    api(libs.androidx.animation)

    api(libs.androidx.navigation.compose)
    api(libs.kotlinx.serialization.json)

    implementation(libs.androidx.paging.paging.compose)

    api(libs.kotlinx.collections.immutable)
}