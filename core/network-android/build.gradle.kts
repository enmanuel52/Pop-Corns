@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.corntime.android.library)
}

android {
    namespace = "com.enmanuelbergling.core.network_android"
}

dependencies {

    implementation(project(":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.org.jetbrains.kotlinx.coroutines)

    //Koin
    implementation(libs.koin.core)

    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.common.ktx)
}