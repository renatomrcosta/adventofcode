import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    application
}
group = "com.xunfos"
version = "1.0"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test-junit5"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
