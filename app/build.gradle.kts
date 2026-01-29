plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mobdev.baonbuddy"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.mobdev.baonbuddy"
        minSdk = 24
        targetSdk = 36
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Navigation Component (for screen transitions & bottom nav)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // SharedPreferences for storing user data
    implementation("androidx.preference:preference-ktx:1.2.1")

    // RecyclerView for avatar grid
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ConstraintLayout (may already be there)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}