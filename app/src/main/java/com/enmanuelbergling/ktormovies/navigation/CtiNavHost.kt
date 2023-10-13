package com.enmanuelbergling.ktormovies.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.enmanuelbergling.ktormovies.CornsTimeAppState
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.moviesGraph
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.navigateToMoviesDetails
import com.enmanuelbergling.ktormovies.ui.screen.series.navigation.seriesGraph

@Composable
fun CtiNavHost(
    state: CornsTimeAppState
) {
    val navController = state.navController

    NavHost(navController = navController, startDestination = state.startDestination) {

        moviesGraph(navController::popBackStack, navController::navigateToMoviesDetails)

        seriesGraph()
    }
}