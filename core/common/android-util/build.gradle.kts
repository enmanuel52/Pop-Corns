plugins {
    alias(libs.plugins.corntime.android.library)
}

android {
    namespace = "com.enmanuelbergling.core.common.android_util"
}

dependencies {

    implementation(project(":core:common:util"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.runtime.ktx)
}