package com.enmanuelbergling.ktormovies.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.enmanuelbergling.ktormovies.CornsTimeAppState
import com.enmanuelbergling.ktormovies.ui.screen.actor.navigation.actorsGraph
import com.enmanuelbergling.ktormovies.ui.screen.actor.navigation.navigateToActorsDetails
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.moviesGraph
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.navigateToMoviesDetails
import com.enmanuelbergling.ktormovies.ui.screen.series.navigation.seriesGraph

import moe.tlaster.precompose.navigation.NavHost as PreNavHost

@Composable
fun CtiNavHost(
    state: CornsTimeAppState,
) {
    val navController = state.navController

    NavHost(navController = navController, startDestination = state.startDestination) {

        moviesGraph(
            navController::popBackStack,
            navController::navigateToMoviesDetails,
            navController::navigateToActorsDetails
        )

        seriesGraph()

        actorsGraph(
            navController::popBackStack,
            navController::navigateToActorsDetails,
            navController::navigateToMoviesDetails
        )
    }
}

@Composable
fun PreCtiNavHost(
    state: CornsTimeAppState,
) {
    val navigator = state.navigator
    PreNavHost(navigator = navigator, initialRoute = state.startDestination) {

        moviesGraph(
            navigator::popBackStack,
            navigator::navigateToMoviesDetails,
            navigator::navigateToActorsDetails
        )
    }
}