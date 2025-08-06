package com.dev.moviesappkmm.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.dev.moviesappkmm.module.initKoin
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.autoreleasepool
import platform.Foundation.NSLog
import platform.UIKit.UIViewController
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
private var isKoinInitialized = false

@OptIn(BetaInteropApi::class)
fun MainViewController(): UIViewController {
    return autoreleasepool {
        try {
            // Initialize Koin for iOS with thread safety and error handling
            if (!isKoinInitialized) {
                initKoin()
                isKoinInitialized = true
                NSLog("Koin initialized successfully")
            }
            
            ComposeUIViewController {
                SafeMoviesApp()
            }
        } catch (e: Exception) {
            NSLog("Critical error in MainViewController: ${e.message}")
            // Return a safe fallback controller
            ComposeUIViewController {
                ErrorFallbackScreen("App initialization failed")
            }
        }
    }
}

@Composable
private fun SafeMoviesApp() {
    LaunchedEffect(Unit) {
        NSLog("Starting SafeMoviesApp composition")
    }
    
    DisposableEffect(Unit) {
        onDispose {
            NSLog("SafeMoviesApp disposed")
        }
    }
    
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MoviesNavigation()
        }
    }
}

@Composable
private fun ErrorFallbackScreen(message: String) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
