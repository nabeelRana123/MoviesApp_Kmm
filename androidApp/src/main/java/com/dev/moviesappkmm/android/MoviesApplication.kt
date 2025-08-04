package com.dev.moviesappkmm.android

import android.app.Application
import com.dev.moviesappkmm.module.initKoin
//import org.koin.android.ext.koin.androidContext
//import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        initKoin {
            // Enable Koin Android logger
//            androidLogger(Level.DEBUG)
//            // Provide Android context to Koin
//            androidContext(this@MoviesApplication)
        }
    }
}
