// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlin_version by extra("1.9.0")
    val ktor_version by extra("1.5.2")
    val nav_version by extra("2.7.7")
    val room_version by extra("2.6.1")
    val coroutines_version by extra("1.4.3")
    val paging_version by extra("3.3.2")

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
        classpath("com.github.triplet.gradle:play-publisher:3.11.0")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id ("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}