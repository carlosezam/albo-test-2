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
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

play {
    serviceAccountCredentials.set(file("../punky.json"))
    track.set("internal") // internal/alpha/beta/production
    //userFraction.set(0.1)
    updatePriority.set(5)
    defaultToAppBundles.set(true)
    resolutionStrategy.set(ResolutionStrategy.AUTO)
    releaseStatus.set(ReleaseStatus.COMPLETED)

}

android {
    namespace = "com.example.punky"

    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if( localPropertiesFile.exists() ) {
        localPropertiesFile.inputStream().use { inputStream ->
            properties.load(inputStream)
        }
    }

    signingConfigs {
        create("GooglePlay") {
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
        versionCode = 39
        versionName = "8"

        testInstrumentationRunner = "com.example.punky.CustomTestRunner"

        ksp{
            arg("room.schemaLocation", "$projectDir/schemas".toString())
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            signingConfig = signingConfigs.getByName("GooglePlay")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            applicationIdSuffix = ".debug"
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
}

dependencies {

    implementation(project(":RickAndMorty"))
    implementation(project(":core"))
    androidTestImplementation(libs.androidx.ui.test.junit4.android)

    androidTestImplementation (libs.androidx.runner)

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
    androidTestImplementation(libs.google.truth)
    //androidTestImplementation(libs.mockk)

    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.coroutines.test)
    testImplementation(libs.mockito.kotlin)

    // compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.constraintlayout)
    implementation(libs.androidx.activity.compose)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // compose utils
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    // compose accompanist
    //implementation(libs.accompanist.drawablepainter)

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
    implementation(libs.activity.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.config.ktx)
    //implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.crashlytics.ktx)

    // google play core
    implementation(libs.play.app.update.ktx)
}

fun getPropOrEnv(entry: String, properties: Properties? = null) : String? {
    return properties?.getProperty(entry, "") ?: System.getenv( entry )
}