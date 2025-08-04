package com.dev.moviesappkmm.data

import com.dev.moviesappkmm.network.MovieApiService

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1): Result<MoviesResponse>
    suspend fun searchMovies(query: String, page: Int = 1): Result<MoviesResponse>
    suspend fun getMovieDetails(movieId: Int): Result<Movie>
}

class MovieRepositoryImpl(
    private val apiService: MovieApiService
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Result<MoviesResponse> {
        return try {
            Result.success(apiService.getPopularMovies(page))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Result<MoviesResponse> {
        return try {
            Result.success(apiService.searchMovies(query, page))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        return try {
            Result.success(apiService.getMovieDetails(movieId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}