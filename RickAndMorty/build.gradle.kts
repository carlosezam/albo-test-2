plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.ezam.rickandmorty"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }

        targetSdk = 34
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.palette)

    //coroutines
    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.roboelectric)

    androidTestImplementation(libs.androidx.junit.ktx)

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

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // compose utils
    implementation(libs.coil.compose)


    // kotlinx serialization
    implementation(libs.serialization.json)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.client.serialization.json)
    //implementation(libs.okhttp.logging.interceptor)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.mock)
    debugImplementation(libs.slf4j)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}