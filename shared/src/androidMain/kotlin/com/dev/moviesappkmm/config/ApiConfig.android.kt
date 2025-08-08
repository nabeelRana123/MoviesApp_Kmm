package com.dev.moviesappkmm.config

import com.dev.moviesappkmm.BuildConfig

actual fun getApiKey(): String = BuildConfig.TMDB_API_KEY
