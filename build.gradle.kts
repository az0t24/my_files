buildscript {
    val composeUiVersion by extra("1.4.0")
    val lifecycleVersion by extra("2.6.1")
    val roomVersion by extra("2.5.0")
}

plugins {
    id("com.android.application") version "8.0.1" apply false
    id("com.android.library") version "8.0.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.10" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jetbrains.kotlin.kapt") version "1.8.10" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}