package com.dev.moviesappkmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform