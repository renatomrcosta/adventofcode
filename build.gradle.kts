import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
}
group = "com.xunfos"
version = "1.0"

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation(kotlin("test-junit5"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}
