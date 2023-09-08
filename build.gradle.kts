
// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false //kt in android
}

//classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
tasks.register("welcome") {
    doLast {
        println("Welcome to Gradle")
    }
}

tasks.register("clean") {
    dependsOn("welcome")
    delete(rootProject.buildDir)
}

tasks.register("my_install") {
    var startTime = 0L
    var endTime = 0L
    println("Start my_install ====>")
    startTime = System.currentTimeMillis()
    dependsOn("app:assembleDebug")
    doLast {
        endTime = System.currentTimeMillis()
        val str = ((endTime - startTime) / 1000)
        println("End my_install <====")
        println("use time ${endTime - startTime}ms")
    }
}