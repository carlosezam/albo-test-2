import com.github.triplet.gradle.androidpublisher.ResolutionStrategy
import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kapt)
    alias(libs.plugins.play.publisher)
    alias(libs.plugins.hilt.android)
}

play {
    serviceAccountCredentials.set(file("../punky.json"))
    track.set("production")
    //userFraction.set(0.1)
    //defaultToAppBundles.set(true)
    resolutionStrategy.set(ResolutionStrategy.AUTO)
    releaseStatus.set(ReleaseStatus.COMPLETED)

}

android {

    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if( localPropertiesFile.exists() ) {
        localPropertiesFile.inputStream().use { inputStream ->
            properties.load(inputStream)
        }
    }

    signingConfigs {
        create("config") {
            if(!getPropOrEnv("STORE_FILE", properties).isNullOrBlank()) {
                storeFile = file( getPropOrEnv("STORE_FILE", properties) ?: "")
                storePassword = getPropOrEnv("STORE_PASSWORD", properties)
                keyAlias = getPropOrEnv("KEY_ALIAS", properties)
                keyPassword = getPropOrEnv("KEY_PASSWORD", properties)
            }
        }
    }

    compileSdk = 34
    //buildToolsVersion "30.0.3"

    defaultConfig {
        applicationId = "com.carlosezam.punky"
        minSdk = 21
        targetSdk = 34
        versionCode = 22
        versionName = "7.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp{
            arg("room.schemaLocation", "$projectDir/schemas".toString())
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("config")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
    packaging {
        // for JNA and JNA-platform
        /*exclude "META-INF/AL2.0"
        exclude "META-INF/LGPL2.1"
        // for byte-buddy
        exclude "META-INF/licenses/ASM"
        pickFirst "win32-x86-64/attach_hotspot_windows.dll"
        pickFirst "win32-x86/attach_hotspot_windows.dll" */
    }
    namespace = "com.example.punky"
}

dependencies {

    implementation(project(":RickAndMorty"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.legacy.support.v4)
    implementation(libs.androidx.activity.compose)

    // testing
    implementation(libs.kotlin.faker)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.ktx)

    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.coroutines.test) {
        // see: https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-debug#debug-agent-and-android
        exclude(group= "org.jetbrains.kotlinx", module= "kotlinx-coroutines-debug")
    }

    testImplementation(libs.mockito.kotlin)

    // ktor
    //implementation("io.ktor:ktor-client-android:$ktor_version")
    //implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.serialization.json)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.okhttp.logging.interceptor)



    // kotlinx serialization
    implementation(libs.serialization.json)

    // ktx
    implementation(libs.lifecycle.runtime.ktx)

    // navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    testImplementation(libs.room.testing)

    implementation(libs.paging.runtime.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}

fun getPropOrEnv(entry: String, properties: Properties? = null) : String? {
    return properties?.getProperty(entry, "") ?: System.getenv( entry )
}