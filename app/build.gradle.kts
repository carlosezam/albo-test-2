import com.github.triplet.gradle.androidpublisher.ResolutionStrategy
import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlinx-serialization")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.github.triplet.play")
}

play {
    serviceAccountCredentials.set(file("../punky.json"))
    track.set("beta")
    userFraction.set(0.1)
    defaultToAppBundles.set(true)
    resolutionStrategy.set(ResolutionStrategy.AUTO)
    releaseStatus.set(ReleaseStatus.IN_PROGRESS)

}

android {

    // load local.properties
    val properties = Properties()
    if( rootProject.file("local.properties").exists() ) {
        //properties.load( rootProject.file("local.properties").newDataInputStream() )
    }

    signingConfigs {
        create("config") {
            if(!getPropOrEnv("STORE_FILE", properties).isNullOrBlank()) {
                storeFile = file( getPropOrEnv("STORE_FILE", properties) ?: "")
            }
            storePassword = getPropOrEnv("STORE_PASSWORD", properties)
            keyAlias = getPropOrEnv("KEY_ALIAS", properties)
            keyPassword = getPropOrEnv("KEY_PASSWORD", properties)
        }
    }

    compileSdk = 34
    //buildToolsVersion "30.0.3"

    defaultConfig {
        applicationId = "com.carlosezam.punky"
        minSdk = 21
        targetSdk = 34
        versionCode = 21
        versionName = "6.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                        "room.schemaLocation" to "$projectDir/schemas".toString(),
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                )
            }
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


    val coroutines_version:String by rootProject.extra
    val ktor_version: String by rootProject.extra
    val nav_version: String by rootProject.extra
    val room_version: String by rootProject.extra
    val paging_version: String by rootProject.extra



    //implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // testing
    implementation("io.github.serpro69:kotlin-faker:1.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    //androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version") {
        // see: https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-debug#debug-agent-and-android
        exclude(group= "org.jetbrains.kotlinx", module= "kotlinx-coroutines-debug")
    }

    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")

    // ktor
    //implementation("io.ktor:ktor-client-android:$ktor_version")
    //implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")



    // kotlinx serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    // ktx
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    ksp("com.github.bumptech.glide:compiler:4.12.0")

    // room
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-paging:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")

    implementation("androidx.paging:paging-runtime-ktx:$paging_version")

    implementation("com.google.dagger:dagger:2.48")
    ksp("com.google.dagger:dagger-compiler:2.48")
}

fun getPropOrEnv(entry: String, properties: Properties? = null) : String? {
    return properties?.getProperty(entry, "") ?: System.getenv( entry )
}