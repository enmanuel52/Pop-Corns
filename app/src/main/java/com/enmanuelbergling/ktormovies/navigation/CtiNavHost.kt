package com.enmanuelbergling.ktormovies.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.enmanuelbergling.feature.actor.navigation.actorsGraph
import com.enmanuelbergling.feature.actor.navigation.navigateToActorsDetails
import com.enmanuelbergling.feature.auth.navigation.loginScreen
import com.enmanuelbergling.feature.movies.navigation.movieSearchScreen
import com.enmanuelbergling.feature.movies.navigation.moviesFilterScreen
import com.enmanuelbergling.feature.movies.navigation.moviesGraph
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesDetails
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesSection
import com.enmanuelbergling.feature.series.navigation.seriesGraph
import com.enmanuelbergling.feature.watchlists.navigation.listGraph
import com.enmanuelbergling.feature.watchlists.navigation.navigateToListDetailsScreen
import com.enmanuelbergling.ktormovies.ui.CornTimeAppState
import com.enmanuelbergling.ktormovies.ui.onSharedItemsDetails
import com.enmanuelbergling.ktormovies.ui.sharedItemsGraph


@Composable
fun CtiNavHost(
    state: CornTimeAppState,
) {
    val navController = state.navController

    NavHost( navController, startDestination = state.startDestination) {

        moviesGraph(
            navController::popBackStack,
            navController::navigateToMoviesDetails,
            navController::navigateToActorsDetails,
            navController::navigateToMoviesSection
        )

        seriesGraph()

        actorsGraph(
            onBack = navController::popBackStack,
            navController::navigateToActorsDetails,
            onMovie = navController::navigateToMoviesDetails
        )

        loginScreen(navController::popBackStack)

        listGraph(
            navController::navigateToListDetailsScreen,
            navController::navigateToMoviesDetails,
            navController::popBackStack
        )

        movieSearchScreen(navController::navigateToMoviesDetails, navController::popBackStack)

        moviesFilterScreen(navController::navigateToMoviesDetails, navController::popBackStack)

        sharedItemsGraph(navController::onSharedItemsDetails)
    }
}