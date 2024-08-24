package com.enmanuelbergling.ktormovies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.enmanuelbergling.feature.actor.navigation.actorsGraph
import com.enmanuelbergling.feature.actor.navigation.navigateToActorsDetails
import com.enmanuelbergling.feature.auth.navigation.loginScreen
import com.enmanuelbergling.feature.auth.navigation.navigateToLoginScreen
import com.enmanuelbergling.feature.movies.navigation.MoviesGraphDestination
import com.enmanuelbergling.feature.movies.navigation.movieSearchScreen
import com.enmanuelbergling.feature.movies.navigation.moviesFilterScreen
import com.enmanuelbergling.feature.movies.navigation.moviesGraph
import com.enmanuelbergling.feature.movies.navigation.navigateToMovieFilter
import com.enmanuelbergling.feature.movies.navigation.navigateToMovieSearch
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesDetails
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesGraph
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesSection
import com.enmanuelbergling.feature.series.navigation.seriesGraph
import com.enmanuelbergling.feature.settings.navigation.settingsGraph
import com.enmanuelbergling.feature.watchlists.navigation.listGraph
import com.enmanuelbergling.feature.watchlists.navigation.navigateToListDetailsScreen
import com.enmanuelbergling.ktormovies.ui.CornTimeAppState


@Composable
fun CtiNavHost(
    state: CornTimeAppState,
    onOpenDrawer: () -> Unit,
) {
    val navController = state.navController

    val context = LocalContext.current

    NavHost(navController, startDestination = state.startDestination) {

        moviesGraph(
            onBack = navController::navigateUp,
            onMovie = navController::navigateToMoviesDetails,
            onActor = { action ->
                navController.navigateToActorsDetails(
                    action.id, action.imageUrl, action.name
                )
            },
            onMore = navController::navigateToMoviesSection,
            onSearch = state.navController::navigateToMovieSearch,
            onFilter = state.navController::navigateToMovieFilter,
            onOpenDrawer = onOpenDrawer,
        )

        seriesGraph(onOpenDrawer = onOpenDrawer)

        actorsGraph(
            onBack = navController::navigateUp,
            onDetails = { action ->
                navController.navigateToActorsDetails(
                    action.id, action.imageUrl, action.name
                )
            },
            onMovie = navController::navigateToMoviesDetails,
            onOpenDrawer = onOpenDrawer,
        )

        loginScreen(
            onLoginSucceed = {
                navController.navigateToMoviesGraph(
                    navOptions {
                        popUpTo(MoviesGraphDestination) {
                            inclusive = true
                        }
                    }
                )
            },
            onBack = navController::navigateUp
        )

        listGraph(
            onDetails = navController::navigateToListDetailsScreen,
            onMovieDetails = navController::navigateToMoviesDetails,
            onAddShortcut = { watchlist -> state.addWatchlistShortcut(context, watchlist) },
            onDeleteShortcut = { watchlistId ->
                state.deleteWatchlistShortcut(
                    context = context,
                    watchlistId = watchlistId
                )
            },
            onBack = navController::navigateUp,
            onOpenDrawer = onOpenDrawer,
        )

        movieSearchScreen(navController::navigateToMoviesDetails, navController::navigateUp)

        moviesFilterScreen(navController::navigateToMoviesDetails, navController::navigateUp)

        settingsGraph(
            onBack = navController::navigateUp,
            onLogin = navController::navigateToLoginScreen
        )
    }
}