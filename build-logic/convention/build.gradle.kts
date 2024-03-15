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
        /*register("androidApplication") {
            id = "buyme.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "buyme.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "buyme.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "buyme.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("jvmLibrary") {
            id = "buyme.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }*/
    }
}