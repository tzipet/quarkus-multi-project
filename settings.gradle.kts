pluginManagement {
    val quarkusPluginVersion: String by settings
    val quarkusPluginId: String by settings
    val nodePluginVersion: String by settings
    val nodePluginId: String by settings

    val kotlinVersion: String by settings

    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.allopen") version kotlinVersion
        kotlin("plugin.noarg") version kotlinVersion

        id(quarkusPluginId) version quarkusPluginVersion
        id(nodePluginId) version nodePluginVersion
    }
}

rootProject.name="quarkus-multi-project"
include("bff-service", "backend-service")
