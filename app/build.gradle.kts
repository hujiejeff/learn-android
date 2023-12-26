@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.application)
    id ("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hujiejeff.learn_android"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner =  "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
//        val signConfig = signingConfigs.getByName("keyStore")
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //            signingConfig = signConfig
        }
    }
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "com.hujiejeff.learn_android"
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {
//    api 'com.szpgm:commonlib:0.0.6'
    implementation(libs.bundles.foundation)
//    implementation("androidx.privacysandbox.tools:tools-core:+")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)

    /**----------------------Glide----------------------*/
    implementation(libs.glide.core)
    ksp(libs.glide.ksp)
    /**---------------------Network--------------------**/
    implementation(libs.bundles.retrofit)

    /**---------------------ViewModel------------------**/
    //Jetpack
    implementation(libs.bundles.mvvm)
    /**----------------compose-------------------------**/
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    debugImplementation(libs.compose.bom)
    implementation(libs.bundles.compose)
    debugImplementation(libs.compose.preview.debug)
    //PictureSelector
    implementation(libs.bundles.pictureSelector)
    //The libary
    implementation(libs.imageviewer)


    val windowmanager_version = "1.0.0"
    implementation("androidx.window:window:$windowmanager_version")
    androidTestImplementation("androidx.window:window-testing:$windowmanager_version")

    implementation("androidx.transition:transition:1.4.1")
//    implementation("io.github.FlyJingFish.OpenImage:OpenImageFullLib:2.0.3")

    //字体
    implementation(libs.bundles.calligraphy)

    //utils
    implementation(libs.utilcodex)

    //RecycleView
    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper:4.0.1")
//    implementation("com.github.hujiejeff:android-base-lib:0.0.2-alpha")
    api("com.github.getActivity:ShapeView:8.3")

    implementation(libs.zxing)
    implementation(libs.zxingLite)

    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.airbnb.android:lottie-compose:6.2.0")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.21.3-beta")
}