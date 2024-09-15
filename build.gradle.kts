plugins {
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.play.publisher) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.apollographql.apollo) apply false
    alias(libs.plugins.sonar)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

// sonarqube/bin/macosx-universal-64/sonar.sh start
subprojects {
    apply(plugin = "org.sonarqube")
    sonar {
        properties {
            property("sonar.host.url", "http://localhost:9000")
            property("sonar.projectKey", "Punky")
            property("sonar.token", System.getenv("SONAR_TOKEN"))

            property("sonar.androidLint.reportPaths", "$buildDir/reports/lint-results-debug.xml")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}