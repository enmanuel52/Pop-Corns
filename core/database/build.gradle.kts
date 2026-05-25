plugins {
    alias(libs.plugins.corntime.android.library)
}

android {
    namespace = "com.enmanuelbergling.core.database"
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.org.jetbrains.kotlinx.coroutines)
}