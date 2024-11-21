plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "de.irmo.fakeandroid_id"
    compileSdk = 34

    defaultConfig {
        applicationId = "de.irmo.fakeandroid_id"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    // Xposed API (only required at compile time)
    compileOnly("de.robv.android.xposed:api:82")

    // Xposed API sources (optional for better IDE support and code navigation)
    //compileOnly("de.robv.android.xposed:api:82:sources")
    implementation("com.google.android.material:material")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}