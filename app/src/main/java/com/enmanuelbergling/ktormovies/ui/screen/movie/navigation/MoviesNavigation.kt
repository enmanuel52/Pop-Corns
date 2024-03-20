package com.enmanuelbergling.ktormovies.ui.screen.movie.navigation

import com.enmanuelbergling.core.model.MovieSection
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.MovieDetailsScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.MoviesScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.NowPlayingMoviesScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.PopularMoviesScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.TopRatedMoviesScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.UpcomingMoviesScreen
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

const val MOVIES_GRAPH_ROUTE = "movies_graph_route"

const val MOVIES_SCREEN_ROUTE = "movies_screen_route"
private const val MOVIES_DETAILS_SCREEN_ROUTE = "movies_details_screen_route"

const val MOVIES_SECTION_SCREEN_ROUTE = "movies_section_screen_route"

private const val ID_ARG = "id_arg"
private const val MOVIE_SECTION_ARG = "movie_section_arg"

fun Navigator.navigateToMoviesGraph(navOptions: NavOptions? = null) {
    navigate(MOVIES_GRAPH_ROUTE, navOptions)
}

fun Navigator.navigateToMoviesDetails(id: Int, navOptions: NavOptions? = null) {
    navigate("/$MOVIES_DETAILS_SCREEN_ROUTE/$id", navOptions)
}

fun Navigator.navigateToMoviesSection(
    movieSection: MovieSection,
    navOptions: NavOptions? = null
) {
    navigate("/$MOVIES_SECTION_SCREEN_ROUTE/$movieSection", navOptions)
}

fun RouteBuilder.moviesGraph(
    onBack: () -> Unit,
    onMovie: (id: Int) -> Unit,
    onActor: (actorId: Int) -> Unit,
    onMore: (MovieSection) -> Unit,
) {
    group(MOVIES_GRAPH_ROUTE, "/$MOVIES_SCREEN_ROUTE") {
        scene(
            "/$MOVIES_SCREEN_ROUTE", navTransition = NavTransition()
        ) {
            MoviesScreen(onDetails = onMovie, onMore)
        }

        scene("/$MOVIES_DETAILS_SCREEN_ROUTE/{$ID_ARG}", navTransition = NavTransition()) {
            val id: Int = it.path(ID_ARG, 0)!!
            MovieDetailsScreen(id = id, onActor, onBack)
        }
        scene(
            "/$MOVIES_SECTION_SCREEN_ROUTE/{$MOVIE_SECTION_ARG}"
        ) {
            val stringSection: String = it.path(MOVIE_SECTION_ARG)!!

            val sectionResult = runCatching { MovieSection.valueOf(stringSection) }
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