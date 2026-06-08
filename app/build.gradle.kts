plugins {

    alias(libs.plugins.android.application)

    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.hilt.android)

    alias(libs.plugins.google.ksp)
}

android {

    namespace = "com.ledge"

    compileSdk = 37

    defaultConfig {

        applicationId = "com.ledge"

        minSdk = 29

        targetSdk = 37

        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner =

            "androidx.test.runner.AndroidJUnitRunner"
    }

    /*
    ---------------------------------------------------
    BUILD TYPES
    ---------------------------------------------------
    */

    buildTypes {

        release {

            isMinifyEnabled = true

            isShrinkResources = true

            proguardFiles(

                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),

                "proguard-rules.pro"
            )
        }

        debug {

            isMinifyEnabled = false

            isShrinkResources = false
        }
    }

    /*
    ---------------------------------------------------
    JAVA / KOTLIN
    ---------------------------------------------------
    */

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_17

        targetCompatibility =
            JavaVersion.VERSION_17
    }



    /*
    ---------------------------------------------------
    COMPOSE
    ---------------------------------------------------
    */

    buildFeatures {

        compose = true
    }

    /*
    ---------------------------------------------------
    PACKAGING
    ---------------------------------------------------
    */

    packaging {

        resources {

            excludes +=

                "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    /*
    ---------------------------------------------------
    CORE ANDROID
    ---------------------------------------------------
    */

    implementation(
        libs.androidx.core.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    implementation(
        libs.androidx.activity.compose
    )

    /*
    ---------------------------------------------------
    COMPOSE BOM
    ---------------------------------------------------
    */

    implementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    androidTestImplementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    /*
    ---------------------------------------------------
    COMPOSE UI
    ---------------------------------------------------
    */

    implementation(
        libs.androidx.compose.ui
    )

    implementation(
        libs.androidx.compose.ui.graphics
    )

    implementation(
        libs.androidx.compose.material3
    )

    implementation(
        libs.androidx.compose.material.icons.extended
    )

    implementation(
        libs.androidx.lifecycle.runtime.compose
    )

    implementation(
        libs.androidx.lifecycle.viewmodel.compose
    )

    /*
    ---------------------------------------------------
    NAVIGATION
    ---------------------------------------------------
    */

    implementation(
        libs.androidx.navigation.compose
    )

    implementation(
        libs.androidx.hilt.navigation.compose
    )

    /*
    ---------------------------------------------------
    COROUTINES
    ---------------------------------------------------
    */

    implementation(
        libs.kotlinx.coroutines.android
    )

    /*
    ---------------------------------------------------
    ROOM DATABASE
    ---------------------------------------------------
    */

    implementation(
        libs.androidx.room.runtime
    )

    implementation(
        libs.androidx.room.ktx
    )

    implementation(
        libs.androidx.room.paging
    )

    ksp(
        libs.androidx.room.compiler
    )

    /*
    ---------------------------------------------------
    PAGING
    ---------------------------------------------------
    */

    implementation(
        libs.androidx.paging.runtime.ktx
    )

    implementation(
        libs.androidx.paging.compose
    )

    /*
    ---------------------------------------------------
    HILT
    ---------------------------------------------------
    */

    implementation(
        libs.hilt.android
    )

    ksp(
        libs.hilt.compiler
    )

    /*
    ---------------------------------------------------
    DATASTORE
    ---------------------------------------------------
    */

    implementation(
        libs.androidx.datastore.preferences
    )

    /*
    ---------------------------------------------------
    BIOMETRICS
    ---------------------------------------------------
    */

    implementation(
        libs.androidx.biometric
    )

    /*
    ---------------------------------------------------
    JSON
    ---------------------------------------------------
    */

    implementation(
        libs.gson
    )

    /*
    ---------------------------------------------------
    TESTING
    ---------------------------------------------------
    */

    testImplementation(
        libs.junit
    )

    androidTestImplementation(
        libs.androidx.junit
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    /*
    ---------------------------------------------------
    DEBUG
    ---------------------------------------------------
    */

    debugImplementation(
        libs.androidx.compose.ui.tooling
    )

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )

    /*
    ---------------------------------------------------
    OPTIONAL / CURRENTLY UNUSED
    ---------------------------------------------------
    */

    // Needed only if using
    // Compose Preview annotations heavily

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )
}