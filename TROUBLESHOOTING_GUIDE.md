# Kotlin Multiplatform Movies App - Troubleshooting Guide

This document contains all the problems encountered during development and their solutions.

## Table of Contents
1. [Build Configuration Issues](#build-configuration-issues)
2. [Serialization Problems](#serialization-problems)
3. [Image Loading Issues](#image-loading-issues)
4. [Dependency Conflicts](#dependency-conflicts)
5. [iOS Specific Issues](#ios-specific-issues)
6. [Animation-Related Crashes](#animation-related-crashes)
7. [Import and Reference Errors](#import-and-reference-errors)

---

## Build Configuration Issues

### Problem 1: Missing Compose Multiplatform Plugin
**Error**: Compose dependencies not recognized
**Solution**: Add the missing plugins to `shared/build.gradle.kts`:
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("plugin.serialization") version "2.0.0"
}
```

### Problem 2: Missing Compose Dependencies
**Error**: Compose UI components not found
**Solution**: Add complete Compose Multiplatform dependencies:
```kotlin
commonMain.dependencies {
    // Compose Multiplatform
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.animation)
    implementation(compose.materialIconsExtended)
    implementation(compose.components.resources)
}
```

---

## Serialization Problems

### Problem 3: Serializer Not Found
**Error**: `kotlinx.serialization.SerializationException: Serializer for class 'MoviesResponse' is not found`
**Solution**: Add the Kotlin Serialization plugin:
```kotlin
plugins {
    kotlin("plugin.serialization") version "2.0.0"
}
```

### Problem 4: String Formatting Issues
**Error**: `Unresolved reference: format`
**Solution**: Replace Java-style formatting with Kotlin-compatible approach:
```kotlin
// Instead of: String.format("%.1f", movie.voteAverage)
// Use this safe helper function:
private fun formatRating(rating: Double): String {
    return try {
        if (rating.isNaN() || rating.isInfinite()) {
            "0.0"
        } else {
            val safeRating = rating.coerceIn(0.0, 10.0)
            (kotlin.math.round(safeRating * 10) / 10).toString()
        }
    } catch (e: Exception) {
        "0.0"
    }
}
```

---

## Image Loading Issues

### Problem 5: Missing Image Loading Library
**Error**: Images not displaying
**Solution**: Add Coil3 dependencies to version catalog and build file:

**In `gradle/libs.versions.toml`:**
```toml
[versions]
coil = "3.0.0"

[libraries]
coil-compose = { module = "io.coil-kt:coil-compose", version.ref = "coil" }
coil-network-ktor = { module = "io.coil-kt:coil-network-ktor", version.ref = "coil" }
```

**In `shared/build.gradle.kts`:**
```kotlin
commonMain.dependencies {
    // Image Loading
    implementation("io.coil-kt.coil3:coil-compose:3.0.0")
    implementation("io.coil-kt.coil3:coil-network-ktor:3.0.0")
}
```

---

## Dependency Conflicts

### Problem 6: Library Conflicts in iOS Build
**Error**: `The same 'unique_name' found in more than one library`
**Solution**: Clean up conflicting dependencies and remove duplicates:
```kotlin
commonMain.dependencies {
    // Remove conflicting dependencies:
    // - compose.animationGraphics (causes conflicts)
    // - compose.components.uiToolingPreview (duplicate modules)
    
    // Keep only essential dependencies
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.animation)
    implementation(compose.materialIconsExtended)
    implementation(compose.components.resources)
}
```

### Problem 7: Java Heap Space Error
**Error**: `Compilation failed: Java heap space`
**Solution**: Increase memory allocation in `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096M -XX:MaxMetaspaceSize=512M -Dfile.encoding=UTF-8 -Dkotlin.daemon.jvm.options\="-Xmx4096M"
org.gradle.caching=true
org.gradle.configuration-cache=true
```

---

## iOS Specific Issues

### Problem 8: "No such module 'shared'" in Xcode
**Error**: iOS app can't find the shared framework
**Solution**: 
1. Build the iOS framework:
   ```bash
   ./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
   ```
2. Add build script to Xcode:
   - Add "Run Script Phase" in Build Phases
   - Script: `cd "$SRCROOT/.." && ./gradlew :shared:embedAndSignAppleFrameworkForXcode`
3. Add framework search paths in Build Settings:
   ```
   $(SRCROOT)/../shared/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)
   ```

### Problem 9: iOS Bundle ID Error
**Error**: `Cannot infer a bundle ID from packages`
**Solution**: Add explicit bundle ID to `shared/build.gradle.kts`:
```kotlin
listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
    it.binaries.framework {
        baseName = "shared"
        isStatic = true
        binaryOption("bundleId", "com.dev.moviesappkmm.shared")
    }
}
```

### Problem 10: PlistSanityCheck Failures
**Error**: `IllegalStateException: Error: Info.plist doesn't have valid entries`
**Solution**: Add required entries to `iosApp/iosApp/Info.plist`:
```xml
<key>UIApplicationSupportsIndirectInputEvents</key>
<true/>
<key>CADisableMinimumFrameDurationOnPhone</key>
<true/>
<key>NSAppTransportSecurity</key>
<dict>
    <key>NSAllowsArbitraryLoads</key>
    <true/>
</dict>
```

---

## Animation-Related Crashes

### Problem 11: iOS Crashes with Animations
**Error**: `Exception __NSCFNumber * 105`, `void * 0x800000010a3f815a`
**Root Cause**: Complex animations cause memory management issues in Kotlin-iOS bridge
**Solution**: **Remove all list animations** for iOS stability:

**Before (Problematic):**
```kotlin
@Composable
private fun MovieItem(movie: Movie, index: Int) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(movie.id) {
        kotlinx.coroutines.delay(index * 50L) // Staggered animation
        isVisible = true
    }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        // Card content
    }
}
```

**After (Stable):**
```kotlin
@Composable
private fun MovieItem(movie: Movie) {
    Card(
        modifier = Modifier.fillMaxWidth().height(180.dp)
        // No animations - direct static content
    ) {
        // Card content
    }
}
```

### Problem 12: Try-Catch Around Composables
**Error**: `Try catch is not supported around composable function invocations`
**Solution**: Remove try-catch blocks from around `@Composable` functions:
```kotlin
// WRONG:
@Composable
fun SafeScreen() {
    try {
        HomeScreen() // This will cause compilation error
    } catch (e: Exception) {
        ErrorScreen()
    }
}

// CORRECT:
@Composable
fun SafeScreen() {
    HomeScreen() // Direct call without try-catch
}
```

---

## Import and Reference Errors

### Problem 13: Missing Icons Import
**Error**: `Unresolved reference: Icons`
**Solution**: Add missing Material Icons imports:
```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
```

### Problem 14: Missing Animation Dependencies
**Error**: `Undefined symbol: androidx.compose.animation.core`
**Solution**: Add animation dependencies to `shared/build.gradle.kts`:
```kotlin
commonMain.dependencies {
    implementation(compose.animation)
    implementation(compose.materialIconsExtended)
}
```

---

## Application Setup

### Problem 15: Missing Android Application Class
**Error**: Koin not initialized on Android
**Solution**: Create `MoviesApplication.kt`:
```kotlin
package com.dev.moviesappkmm.android

import android.app.Application
import com.dev.moviesappkmm.module.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@MoviesApplication)
        }
    }
}
```

### Problem 16: Missing Internet Permission
**Error**: Network requests fail on Android
**Solution**: Add to `androidApp/src/main/AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## iOS Platform Dependencies

### Problem 17: Missing iOS Ktor Client
**Error**: Network requests fail on iOS
**Solution**: Add iOS-specific Ktor client:
```kotlin
iosMain.dependencies {
    implementation("io.ktor:ktor-client-darwin:2.3.7")
}
```

### Problem 18: HttpTimeout Plugin Missing
**Error**: `NoClassDefFoundError: Failed resolution of: Lio/ktor/client/plugins/HttpTimeout`
**Solution**: Add missing Ktor timeout plugin dependency:

**In `gradle/libs.versions.toml`:**
```toml
ktor-client-timeout = { module = "io.ktor:ktor-client-timeout", version.ref = "ktorClientCore" }
```

**In `shared/build.gradle.kts`:**
```kotlin
commonMain.dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.timeout)  // Add this line
}
```

---

## Key Lessons Learned

### 1. **Animation Complexity = iOS Instability**
- Complex animations (staggered, combined effects) cause crashes on iOS
- Simple static lists are more stable across platforms
- Start with basic functionality, add animations carefully

### 2. **Platform-Specific Dependencies**
- Always add platform-specific network clients (Android/Darwin)
- iOS requires additional plist configurations for Compose
- Memory management differs between platforms

### 3. **Dependency Management**
- Keep dependencies minimal and avoid duplicates
- Use version catalogs for consistency
- Clean up unused animation dependencies

### 4. **Error Handling Patterns**
- Don't use try-catch around `@Composable` functions
- Handle errors at the data/business logic level
- Use safe number formatting for cross-platform compatibility

---

## Quick Setup Checklist for New Projects

### ✅ Build Configuration
- [ ] Add Compose Multiplatform plugin
- [ ] Add Kotlin Serialization plugin
- [ ] Configure memory settings in gradle.properties
- [ ] Add platform-specific dependencies (Android/iOS)

### ✅ iOS Configuration
- [ ] Add bundle ID to framework configuration
- [ ] Update Info.plist with required entries
- [ ] Set up Xcode build phases
- [ ] Add framework search paths

### ✅ Dependencies
- [ ] Add image loading library (Coil3)
- [ ] Add Material Icons Extended
- [ ] Add proper Ktor clients for each platform
- [ ] Avoid conflicting animation dependencies

### ✅ Code Patterns
- [ ] Use safe number formatting functions
- [ ] Avoid complex animations on iOS
- [ ] Keep Composable functions simple
- [ ] Handle errors outside of UI composition

---

## Final Notes

**The most important lesson**: When targeting iOS with Compose Multiplatform, **start simple and add complexity gradually**. Complex animations that work fine on Android can cause crashes on iOS due to different memory management and threading models.

**For production apps**: Prioritize stability over fancy animations. Users prefer a fast, reliable app over one with beautiful animations that crashes.

Generated on: July 21, 2025
Project: Movies App KMM with Compose Multiplatform
