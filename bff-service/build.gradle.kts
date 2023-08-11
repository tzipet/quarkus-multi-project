plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    id("io.quarkus")
    id("com.github.node-gradle.node")
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val nodeVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-resteasy-reactive")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}

node {
    version.set(nodeVersion)
    download.set(true)
    workDir.set(file("${project.rootDir}/.cache/nodejs"))
    nodeProjectDir.set(file("${project.projectDir}/src/main/webapp"))
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("buildReact") {
    dependsOn(tasks.npmInstall)
    npmCommand.set(listOf("run", "build"))
    environment.set(mapOf("BUILD_PATH" to "${buildDir}/resources/main/META-INF/resources"))

    inputs.files(fileTree("${project.projectDir}/src/main/webapp") {
        exclude("node_modules")
    })
    outputs.dir("${buildDir}/resources/main/META-INF/resources")
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("startReact") {
    dependsOn(tasks.npmInstall)
    npmCommand.set(listOf("run", "start"))

    inputs.dir("${project.projectDir}/src/main/webapp")
}

tasks.getByName("processResources").dependsOn(tasks.getByName("buildReact"))