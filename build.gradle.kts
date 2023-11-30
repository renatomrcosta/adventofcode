import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"
    application
}
group = "com.xunfos"
version = "1.0"

repositories {
    mavenCentral()
}
dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation(kotlin("test-junit5"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}
