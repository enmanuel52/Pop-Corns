package com.enmanuelbergling.feature.movies.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.enmanuelbergling.core.model.MovieSection
import com.enmanuelbergling.core.ui.components.topComposable
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.movies.details.MovieDetailsScreen
import com.enmanuelbergling.feature.movies.filter.MoviesFilterRoute
import com.enmanuelbergling.feature.movies.home.MoviesScreen
import com.enmanuelbergling.feature.movies.list.NowPlayingMoviesScreen
import com.enmanuelbergling.feature.movies.list.PopularMoviesScreen
import com.enmanuelbergling.feature.movies.list.TopRatedMoviesScreen
import com.enmanuelbergling.feature.movies.list.UpcomingMoviesScreen
import com.enmanuelbergling.feature.movies.search.MovieSearchScreen
import kotlinx.serialization.Serializable


@Serializable
data object MoviesGraphDestination

@Serializable
data object MoviesDestination

@Serializable
data class MoviesDetailsDestination(val id: Int)

@Serializable
data class MoviesSectionDestination(val section: String)

fun NavHostController.navigateToMoviesGraph(navOptions: NavOptions? = null) {
    navigate(MoviesGraphDestination, navOptions)
}

fun NavHostController.navigateToMoviesDetails(id: Int, navOptions: NavOptions? = null) {
    navigate(MoviesDetailsDestination(id), navOptions)
}

fun NavHostController.navigateToMoviesSection(
    movieSection: MovieSection,
    navOptions: NavOptions? = null,
) {
    navigate(MoviesSectionDestination("$movieSection"), navOptions)
}

fun NavGraphBuilder.moviesGraph(
    onBack: () -> Unit,
    onMovie: (id: Int) -> Unit,
    onActor: (ActorDetailNavAction) -> Unit,
    onMore: (MovieSection) -> Unit,
    onSearch: () -> Unit,
    onFilter: () -> Unit,
    onOpenDrawer: () -> Unit,
) {
    navigation<MoviesGraphDestination>(startDestination = MoviesDestination) {

        topComposable<MoviesDestination> {
            MoviesScreen(
                onDetails = onMovie,
                onMore = onMore,
                onSearch = onSearch,
                onFilter = onFilter,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable<MoviesDetailsDestination> { backStackEntry ->
            val id = backStackEntry.toRoute<MoviesDetailsDestination>().id
            MovieDetailsScreen(id = id, onActor, onBack)
        }
        composable<MoviesSectionDestination> { backStackEntry ->
            val stringSection: String = backStackEntry.toRoute<MoviesSectionDestination>().section

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

fun NavHostController.navigateToMovieFilter(
    navOptions: NavOptions? = null,
) {
    navigate(MoviesFilterDestination, navOptions)
}

@Serializable
data object MoviesFilterDestination

fun NavGraphBuilder.moviesFilterScreen(
    onMovie: (id: Int) -> Unit,
    onBack: () -> Unit,
) {
    composable<MoviesFilterDestination> {
        MoviesFilterRoute(onBack = onBack, onMovie = onMovie)
    }
}


fun NavHostController.navigateToMovieSearch(
    navOptions: NavOptions? = null,
) {
    navigate(MovieSearchDestination, navOptions)
}

@Serializable
data object MovieSearchDestination

fun NavGraphBuilder.movieSearchScreen(
    onMovie: (id: Int) -> Unit,
    onBack: () -> Unit,
) {
    composable<MovieSearchDestination> {
        MovieSearchScreen(onMovieDetails = onMovie, onBack)
    }
}