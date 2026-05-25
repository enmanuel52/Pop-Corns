import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.13.1")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.21")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
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