plugins {
    id("com.android.application")
    id ("org.jetbrains.kotlin.android")
//    id 'kotlin-kapt'
    id("com.google.devtools.ksp")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.hujiejeff.learn_android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =  "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
//        val signConfig = signingConfigs.getByName("keyStore")
        getByName("release") {
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
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.privacysandbox.tools:tools-core:+")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")


    val glide_version = "4.16.0"
    /**----------------------Glide----------------------*/
    implementation("com.github.bumptech.glide:glide:$glide_version")
    ksp("com.github.bumptech.glide:ksp:$glide_version")

    /**---------------------Network--------------------**/
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")


    /**---------------------ViewModel------------------**/
    //Jetpack
    val lifecycle_version = "2.5.1"
    val arch_version = "2.1.0"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Annotation processor
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")


    /**-------------------activity---------------------**/
    val activity_version = "1.5.1"
    implementation("androidx.activity:activity-ktx:$activity_version")
    val fragment_version = "1.5.2"
    // Kotlin
    implementation("androidx.fragment:fragment-ktx:$fragment_version")


    //PictureSelector

    // PictureSelector basic (Necessary)
    implementation("io.github.lucksiege:pictureselector:v3.11.0")

    // image compress library (Not necessary)
    implementation("io.github.lucksiege:compress:v3.10.9")

    // uCrop library (Not necessary)
    implementation("io.github.lucksiege:ucrop:v3.10.9")

    // simple camerax library (Not necessary)
    implementation("io.github.lucksiege:camerax:v3.10.9")

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
}