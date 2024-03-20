plugins {
    alias(libs.plugins.corntime.android.library)
    alias(libs.plugins.com.google.protobuf)
}
android {
    namespace = "com.enmanuelbergling.core.datastore"
}

dependencies {

    api(project(":core:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.org.jetbrains.kotlinx.coroutines)

    //DataStore
    implementation(libs.androidx.datastore.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.com.google.protobuf.javalite)

    //Koin
    implementation(libs.koin.core)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
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