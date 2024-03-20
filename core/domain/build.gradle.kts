plugins {
    alias(libs.plugins.corntime.jvm.library)
}

dependencies {

    api(project(":core:model"))

    implementation(libs.org.jetbrains.kotlinx.coroutines)
}