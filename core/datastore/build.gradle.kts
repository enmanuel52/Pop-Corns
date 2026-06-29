plugins {
    alias(libs.plugins.corntime.android.library)
    alias(libs.plugins.com.google.protobuf)
    id("org.jetbrains.kotlin.plugin.serialization")
}
android {
    namespace = "com.enmanuelbergling.core.datastore"
}

dependencies {

    implementation(project(":core:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.org.jetbrains.kotlinx.coroutines)

    //DataStore
    implementation(libs.androidx.datastore.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.com.google.protobuf.javalite)

    implementation(libs.kotlinx.serialization.json)

    //Koin
    implementation(libs.koin.core)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0:osx-x86_64"
    }

    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}