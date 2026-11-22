pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "AlarmApp"

// Core modules
include("core:domain")
include("core:data")
include("core:platform")

// Feature modules
include("feature:alarm")
include("feature:sleep")
include("feature:settings")
include("feature:developer")
include("feature:onboarding")

// Platform modules
include("wear")
include("auto")

// App module
include("app")

// Plugin API module
include("plugin-api")
