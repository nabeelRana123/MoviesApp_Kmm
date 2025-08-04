package com.dev.moviesappkmm.module

import com.dev.moviesappkmm.data.MovieRepository
import com.dev.moviesappkmm.data.MovieRepositoryImpl
import com.dev.moviesappkmm.network.MovieApiService
import com.dev.moviesappkmm.network.MovieApiServiceImpl
import com.dev.moviesappkmm.network.createHttpClient
import com.dev.moviesappkmm.presentation.MoviesViewModel
import com.dev.moviesappkmm.presentation.usecase.GetPopularMoviesUseCase
import com.dev.moviesappkmm.presentation.usecase.SearchMoviesUseCase
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single<MovieApiService> { MovieApiServiceImpl(get()) }
}

val dataModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}

val domainModule = module {
    factory<GetPopularMoviesUseCase> { GetPopularMoviesUseCase(get()) }
    factory<SearchMoviesUseCase> { SearchMoviesUseCase(get()) }
}

val uiModule = module {
    factory<MoviesViewModel> { MoviesViewModel(get(), get()) }
    //factory<MovieDetailViewModel> { MovieDetailViewModel(get()) }
}

val allModules = listOf(
    networkModule,
    dataModule,
    domainModule,
    uiModule
)