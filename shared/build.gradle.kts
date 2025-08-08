import org.gradle.kotlin.dsl.invoke
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("plugin.serialization") version "2.0.0"
}

// Load API key from local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val tmdbApiKey = localProperties.getProperty("tmdb.api.key") ?: "your_api_key_here"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
            
            // Set explicit bundle ID to resolve warning
            binaryOption("bundleId", "com.dev.moviesappkmm.shared")

            // Export all necessary dependencies for iOS
            export(libs.jetbrains.navigation.compose)
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.koin.core)
            implementation(libs.koin.androidx.compose)
            implementation(libs.koin.composeVM)

            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.animation)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            // Kotlinx Serialization
            implementation(libs.kotlinx.serialization.json)

            // Lifecycle and Coroutines
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.kotlinx.coroutines.core)

            // Image Loading
            implementation(libs.coil3.coil.compose)
            implementation(libs.coil.network.okhttp)

            // navigation - JetBrains Compose Multiplatform Navigation
            api(libs.jetbrains.navigation.compose)  // Use api() to make it available to iOS
        }

        androidMain.dependencies {
            //ktor android
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.okhttp)
            implementation(compose.uiTooling)

            // koin android - remove duplicates
            implementation(libs.koin.android)
        }
        
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.dev.moviesappkmm"
    compileSdk = 35
    defaultConfig {
        minSdk = 24

        // Make API key available as a build config field
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.glance.preview)
}
