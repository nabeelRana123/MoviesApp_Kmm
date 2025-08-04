package com.dev.moviesappkmm.presentation.usecase

import com.dev.moviesappkmm.data.MovieRepository
import com.dev.moviesappkmm.data.MoviesResponse

class GetPopularMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int = 1): Result<MoviesResponse> {
        return repository.getPopularMovies(page)
    }
}

class SearchMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String, page: Int = 1): Result<MoviesResponse> {
        return repository.searchMovies(query, page)
    }
}