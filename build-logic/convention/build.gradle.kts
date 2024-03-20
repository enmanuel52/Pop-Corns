import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.1.0")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}

tasks {
    validatePlugins {
        enabled  = true
    }
}

gradlePlugin{
    plugins {
        register("androidApplication") {
            id = "corntime.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "corntime.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "corntime.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("jvmLibrary") {
            id = "corntime.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}