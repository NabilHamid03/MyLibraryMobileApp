import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.0"
}

val localProperties = Properties()
val localPropertiesFile = File(rootDir,"secret.properties")
if(localPropertiesFile.exists() && localPropertiesFile.isFile){
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}


android {
    namespace = "he2b.be.mylibrary"
    compileSdk = 35

    defaultConfig {
        applicationId = "he2b.be.mylibrary"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String","SUPABASE_KEY",localProperties.getProperty("SUPABASE_KEY"))
        buildConfigField("String","GOOGLE_KEY",localProperties.getProperty("GOOGLE_KEY"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.ktor:ktor-client-android:3.1.2")
    implementation("io.github.jan-tennert.supabase:auth-kt")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    implementation ("androidx.camera:camera-core:1.4.2")
    implementation ("androidx.camera:camera-camera2:1.4.2")
    implementation ("androidx.camera:camera-lifecycle:1.4.2")
    implementation ("androidx.camera:camera-view:1.4.2")
    implementation ("com.google.mlkit:barcode-scanning:17.3.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.0")
}