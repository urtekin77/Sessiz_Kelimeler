plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.tidtanima"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tidtanima"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }


    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.gridlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.0")
    implementation ("androidx.work:work-runtime:2.7.1")
    implementation ("androidx.profileinstaller:profileinstaller:1.2.0")

    implementation(libs.media3.exoplayer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}