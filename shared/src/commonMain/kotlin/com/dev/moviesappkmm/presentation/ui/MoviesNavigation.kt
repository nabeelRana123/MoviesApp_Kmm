package com.dev.moviesappkmm.presentation.ui


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dev.moviesappkmm.presentation.MoviesViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.serialization.Serializable

// Define type-safe navigation routes
@Serializable
object Splash

@Serializable
object Home

@Serializable
data class MovieDetail(val movieId: Int)

@Composable
fun MoviesNavigation(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MoviesViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = Splash
    ) {
        composable<Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Home) {
                        popUpTo<Splash> { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            HomeScreen(
                viewModel = viewModel,
                onMovieClick = { movie ->
                    viewModel.selectMovie(movie)
                    navController.navigate(MovieDetail(movieId = movie.id))
                }
            )
        }

        composable<MovieDetail> { backStackEntry ->
            val movieDetail = backStackEntry.toRoute<MovieDetail>()
            MovieDetailScreen(
                movieId = movieDetail.movieId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
