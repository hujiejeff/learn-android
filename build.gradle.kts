import org.gradle.internal.typeconversion.TimeUnitsParser

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.2" apply false
    id("com.android.library") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
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