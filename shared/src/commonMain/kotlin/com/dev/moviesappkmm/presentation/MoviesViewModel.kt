package com.dev.moviesappkmm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.moviesappkmm.data.Movie
import com.dev.moviesappkmm.presentation.usecase.GetPopularMoviesUseCase
import com.dev.moviesappkmm.presentation.usecase.SearchMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadPopularMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            getPopularMoviesUseCase().fold(
                onSuccess = { response ->
                    _movies.value = response.results
                },
                onFailure = { error ->
                    error.printStackTrace()
                }
            )
            _isLoading.value = false
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            searchMoviesUseCase(query).fold(
                onSuccess = { response ->
                    _movies.value = response.results
                },
                onFailure = { error ->
                    // Handle error
                }
            )
            _isLoading.value = false
        }
    }
}