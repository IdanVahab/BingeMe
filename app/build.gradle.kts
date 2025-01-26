plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android") // ודא שהפלאגין של Hilt מופיע כאן
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.example.bingeme"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bingeme"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    buildFeatures {
        compose = true
        viewBinding {
            enable = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    kapt {
        correctErrorTypes = true
        useBuildCache = true
        arguments {
            arg("room.incremental", "true") // פרמטרים שדרושים ל-Room
            arg("dagger.fastInit", "enabled") // למקרים מיוחדים של Dagger
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.support.annotations)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // LifeCycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Glide
    implementation(libs.glide)
    kapt(libs.compiler)

    // Shimmer

    // Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Dagger-Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // For instrumentation tests
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    // For local unit tests
    testImplementation (libs.hilt.android.testing.v255)
    kaptTest (libs.dagger.hilt.compiler)



    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // Google Play Services

    // JUnit
    implementation(libs.junit)

    // Espresso

    // ConstraintLayout
    implementation(libs.androidx.constraintlayout.v214)

    // Compose
    implementation(libs.androidx.material3)

    // Compose Runtime ו-UI
    implementation (libs.androidx.ui)
    implementation (libs.androidx.ui.tooling)
    implementation (libs.androidx.activity.compose)


}