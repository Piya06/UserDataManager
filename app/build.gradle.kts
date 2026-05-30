plugins {
    alias(libs.plugins.android.application)

    alias(libs.plugins.kotlin.android)

    //alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)

    alias(libs.plugins.hilt.android)

    alias(libs.plugins.navigation.safeargs)
    id("kotlin-parcelize")
}

android {
    namespace = "com.userdata.manager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.userdata.manager"
        minSdk = 24
        targetSdk = 35
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
       // sourceCompatibility = JavaVersion.VERSION_11
       // targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

 /*   kotlinOptions {
        jvmTarget = "17"
    }*/



    kotlin { jvmToolchain(17) }

    buildFeatures {
        dataBinding = true
    }

    sourceSets {
        getByName("main") {
            java.srcDir("build/generated/ksp/debug/kotlin")
        }
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    // Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    //kapt(libs.androidx.room.compiler)

    ksp(libs.androidx.room.compiler)
    //kapt(libs.sqlite.jdbc)

   // implementation(libs.sqlite.jdbc)

    // Hilt
    implementation(libs.hilt.android)
  //  kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler)
    //kapt { correctErrorTypes = true }


    // Activity / Fragment
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
}
