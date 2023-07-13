
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Reet"
include(":app")
include(":core:ui")
include(":core:navigation")
include(":data")
include(":feature:auth")
include(":feature:new_report")
include(":feature:news")
include(":feature:account")
include(":feature:home")