package com.dev.moviesappkmm.network

import com.dev.moviesappkmm.config.getApiKey
import com.dev.moviesappkmm.data.Movie
import com.dev.moviesappkmm.data.MoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface MovieApiService {
    suspend fun getPopularMovies(page: Int = 1): MoviesResponse
    suspend fun searchMovies(query: String, page: Int = 1): MoviesResponse
    suspend fun getMovieDetails(movieId: Int): Movie
}

class MovieApiServiceImpl(
    private val client: HttpClient
) : MovieApiService {

    private val baseUrl = "https://api.themoviedb.org/3"

    override suspend fun getPopularMovies(page: Int): MoviesResponse {
        return client.get("$baseUrl/movie/popular") {
            parameter("api_key", getApiKey())
            parameter("page", page)
        }.body()
    }

    override suspend fun searchMovies(query: String, page: Int): MoviesResponse {
        return client.get("$baseUrl/search/movie") {
            parameter("api_key", getApiKey())
            parameter("query", query)
            parameter("page", page)
        }.body()
    }

    override suspend fun getMovieDetails(movieId: Int): Movie {
        return client.get("$baseUrl/movie/$movieId") {
            parameter("api_key", getApiKey())
        }.body()
    }
}