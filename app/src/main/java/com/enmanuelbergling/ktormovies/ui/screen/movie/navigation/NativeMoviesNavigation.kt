package com.enmanuelbergling.ktormovies.ui.screen.movie.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.enmanuelbergling.ktormovies.domain.model.MovieSection
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.MovieDetailsScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.MoviesScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.NowPlayingMoviesScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.PopularMoviesScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.TopRatedMoviesScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.UpcomingMoviesScreen


private const val MOVIES_DETAILS_SCREEN_ROUTE = "movies_details_screen_route"

private const val ID_ARG = "id_arg"
private const val MOVIE_SECTION_ARG = "movie_section_arg"

fun NavHostController.navigateToMoviesGraph(navOptions: NavOptions? = null) {
    navigate(MOVIES_GRAPH_ROUTE, navOptions)
}

fun NavHostController.navigateToMoviesDetails(id: Int, navOptions: NavOptions? = null) {
    navigate("$MOVIES_DETAILS_SCREEN_ROUTE/$id", navOptions)
}

fun NavHostController.navigateToMoviesSection(
    movieSection: MovieSection,
    navOptions: NavOptions? = null,
) {
    navigate("$MOVIES_SECTION_SCREEN_ROUTE/$movieSection", navOptions)
}

fun NavGraphBuilder.moviesGraph(
    onBack: () -> Unit,
    onMovie: (id: Int) -> Unit,
    onActor: (actorId: Int) -> Unit,
    onMore: (MovieSection) -> Unit,
) {
    navigation(startDestination = MOVIES_SCREEN_ROUTE, route = MOVIES_GRAPH_ROUTE) {
        composable(MOVIES_SCREEN_ROUTE) {
            MoviesScreen(onDetails = onMovie, onMore)
        }

        composable(
            "$MOVIES_DETAILS_SCREEN_ROUTE/{$ID_ARG}", arguments = listOf(
                navArgument(ID_ARG) {
                    type = NavType.IntType
                }
            )) {
            val id = it.arguments!!.getInt(ID_ARG)
            MovieDetailsScreen(id = id, onActor, onBack)
        }

        composable(
            "$MOVIES_SECTION_SCREEN_ROUTE/{$MOVIE_SECTION_ARG}", arguments = listOf(
                navArgument(MOVIE_SECTION_ARG) {
                    type = NavType.StringType
                }
            )) {
            val stringSection = it.arguments?.getString(MOVIE_SECTION_ARG)

            val sectionResult = runCatching { MovieSection.valueOf(stringSection!!) }
            sectionResult.onSuccess { result ->
                when (result) {
                    MovieSection.Upcoming -> UpcomingMoviesScreen(onMovie = onMovie, onBack)
                    MovieSection.NowPlaying -> NowPlayingMoviesScreen(
                        onMovie = onMovie,
                        onBack = onBack
                    )

                    MovieSection.TopRated -> TopRatedMoviesScreen(
                        onMovie = onMovie,
                        onBack = onBack
                    )

                    MovieSection.Popular -> PopularMoviesScreen(
                        onMovie = onMovie,
                        onBack = onBack
                    )
                }
            }.onFailure { onBack() }
        }
    }
}