@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.application)
    id ("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.hujiejeff.learn_android"
        minSdk = 21
        targetSdk = 33
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
    /*compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }*/
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding.isEnabled = true
    namespace = "com.hujiejeff.learn_android"
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
    //PictureSelector
    implementation(libs.bundles.pictureSelector)
    //The libary
    implementation("com.github.iielse:imageviewer:2.1.23")


    val windowmanager_version = "1.0.0"
    implementation("androidx.window:window:$windowmanager_version")
    androidTestImplementation("androidx.window:window-testing:$windowmanager_version")

    implementation("androidx.transition:transition:1.4.1")
    implementation("io.github.FlyJingFish.OpenImage:OpenImageFullLib:2.0.3")

    //字体
    implementation("io.github.inflationx:calligraphy3:3.1.1")
    implementation("io.github.inflationx:viewpump:2.0.3")

    //utils
    implementation("com.blankj:utilcodex:1.31.1")

    //RecycleView
    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper:4.0.1")
//    implementation("com.github.hujiejeff:android-base-lib:0.0.2-alpha")
//    api("com.github.getActivity:ShapeView:8.3")
}