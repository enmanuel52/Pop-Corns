package com.enmanuelbergling.ktormovies.ui.screen.movie.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.MovieDetailsScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.MoviesScreen
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModel

const val MOVIES_GRAPH_ROUTE = "movies_graph_route"

const val MOVIES_SCREEN_ROUTE = "movies_screen_route"
private const val MOVIES_DETAILS_SCREEN_ROUTE = "movies_details_screen_route"

private const val ID_ARG = "id_arg"

fun NavHostController.navigateToMoviesGraph(navOptions: NavOptions? = null) {
    navigate(MOVIES_GRAPH_ROUTE, navOptions)
}

fun NavHostController.navigateToMoviesDetails(id: Int, navOptions: NavOptions? = null) {
    navigate("$MOVIES_DETAILS_SCREEN_ROUTE/$id", navOptions)
}

fun Navigator.navigateToMoviesDetails(id: Int) {
    navigate("/$MOVIES_DETAILS_SCREEN_ROUTE/$id")
}

fun NavGraphBuilder.moviesGraph(onBack: () -> Unit, onMovie: (id: Int) -> Unit) {
    navigation(startDestination = MOVIES_SCREEN_ROUTE, route = MOVIES_GRAPH_ROUTE) {
        composable(MOVIES_SCREEN_ROUTE) {
            MoviesScreen(onDetails = onMovie)
        }

        composable(
            "$MOVIES_DETAILS_SCREEN_ROUTE/{$ID_ARG}", arguments = listOf(
                navArgument(ID_ARG) {
                    type = NavType.IntType
                }
            )) {
            val id = it.arguments!!.getInt(ID_ARG)
            MovieDetailsScreen(id = id, onBack)
        }
    }
}

fun RouteBuilder.moviesGraph(onBack: () -> Unit, onMovie: (id: Int) -> Unit) {
    group(MOVIES_GRAPH_ROUTE, "/$MOVIES_SCREEN_ROUTE") {
        scene(
            "/$MOVIES_SCREEN_ROUTE", navTransition = NavTransition()
        ) {
            MoviesScreen(onDetails = onMovie)
        }

        scene("/$MOVIES_DETAILS_SCREEN_ROUTE/{$ID_ARG}", navTransition = NavTransition()) {
            val id: Int = it.path(ID_ARG, 0)!!
            MovieDetailsScreen(id = id, onBack)
        }
    }
}