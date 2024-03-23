pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url= uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url= uri("https://jitpack.io") }
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

rootProject.name = "Ktor Movies"
include(":app")
include(":core:domain")
include(":core:model")
include(":core:datastore")
include(":core:ui")
include(":core:database")
include(":core:common:android-util")
include(":core:common:util")
include(":core:network")
include(":feature:actor")
include(":feature:auth")
include(":feature:movies")
include(":feature:series")
include(":feature:watchlists")
