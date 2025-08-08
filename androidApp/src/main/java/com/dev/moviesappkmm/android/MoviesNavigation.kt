package com.dev.moviesappkmm.android

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.moviesappkmm.presentation.ui.HomeScreen
import com.dev.moviesappkmm.presentation.ui.MovieDetailScreen
import com.dev.moviesappkmm.presentation.MoviesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MoviesNavigation(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MoviesViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onMovieClick = { movie ->
                    viewModel.selectMovie(movie)
                    navController.navigate("movie_detail/${movie.id}")
                }
            )
        }

        composable("movie_detail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: 0
            MovieDetailScreen(
                movieId = movieId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
