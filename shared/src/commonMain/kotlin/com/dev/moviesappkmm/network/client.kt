package com.dev.moviesappkmm.network

import io.ktor.client.HttpClient

// Platform-specific HTTP client creation
expect fun createHttpClient(): HttpClient