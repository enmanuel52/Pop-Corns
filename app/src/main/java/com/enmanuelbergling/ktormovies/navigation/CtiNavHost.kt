package com.enmanuelbergling.ktormovies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.enmanuelbergling.feature.actor.navigation.actorsGraph
import com.enmanuelbergling.feature.actor.navigation.navigateToActorsDetails
import com.enmanuelbergling.feature.auth.navigation.loginScreen
import com.enmanuelbergling.feature.auth.navigation.navigateToLoginScreen
import com.enmanuelbergling.feature.favorites.navigation.favoritesGraph
import com.enmanuelbergling.feature.favorites.navigation.navigateToFavorites
import com.enmanuelbergling.feature.movies.navigation.MoviesGraphDestination
import com.enmanuelbergling.feature.movies.navigation.moviesFilterScreen
import com.enmanuelbergling.feature.movies.navigation.moviesGraph
import com.enmanuelbergling.feature.movies.navigation.navigateToMovieFilter
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesDetails
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesGraph
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesSection
import com.enmanuelbergling.feature.series.navigation.navigateToFavoriteSeries
import com.enmanuelbergling.feature.series.navigation.navigateToSeriesDetailFlow
import com.enmanuelbergling.feature.series.navigation.navigateToSeriesSection
import com.enmanuelbergling.feature.series.navigation.seriesGraph
import com.enmanuelbergling.feature.settings.navigation.settingsGraph
import com.enmanuelbergling.feature.watchlists.navigation.listGraph
import com.enmanuelbergling.feature.watchlists.navigation.navigateToListDetailsScreen
import com.enmanuelbergling.feature.watchlists.navigation.navigateToListsScreen
import com.enmanuelbergling.feature.watchlists.navigation.navigateToWatchlistSeriesTab
import com.enmanuelbergling.ktormovies.ui.CornTimeAppState


@Composable
fun CtiNavHost(
    state: CornTimeAppState,
    modifier: Modifier = Modifier,
    onOpenDrawer: () -> Unit,
) {
    val navController = state.navController

    val context = LocalContext.current

    NavHost(
        navController,
        startDestination = state.startDestination,
        modifier = modifier,
        ) {

        moviesGraph(
            onBack = navController::navigateUp,
            onMovie = navController::navigateToMoviesDetails,
            onActor = { action ->
                navController.navigateToActorsDetails(
                    action.id, action.imageUrl, action.name
                )
            },
            onMore = navController::navigateToMoviesSection,
            onFilter = state.navController::navigateToMovieFilter,
            onFavorites = navController::navigateToFavorites,
            onOpenDrawer = onOpenDrawer,
        )

        seriesGraph(
            onSeries = navController::navigateToSeriesDetailFlow,
            onSection = navController::navigateToSeriesSection,
            onFavorites = navController::navigateToFavoriteSeries,
            onActor = { action ->
                navController.navigateToActorsDetails(
                    action.id, action.imageUrl, action.name
                )
            },
            onBack = navController::navigateUp,
            onOpenDrawer = onOpenDrawer,
        )

        favoritesGraph(
            onMovieDetails = navController::navigateToMoviesDetails,
            onBack = navController::navigateUp,
        )

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
            onNavigateToLists = navController::navigateToListsScreen,
            onMovieDetails = navController::navigateToMoviesDetails,
            onSeriesDetails = navController::navigateToSeriesDetailFlow,
            onAddShortcut = { watchlist -> state.addWatchlistShortcut(context, watchlist) },
            onDeleteShortcut = { watchlistId ->
                state.deleteWatchlistShortcut(
                    context = context,
                    watchlistId = watchlistId
                )
            },
            onBack = navController::navigateUp,
        )

        moviesFilterScreen(navController::navigateToMoviesDetails, navController::navigateUp)

        settingsGraph(
            onBack = {
                state.navigateToDrawerDestination(TopDestination.Movies)
            },
            onLogin = navController::navigateToLoginScreen
        )
    }
}