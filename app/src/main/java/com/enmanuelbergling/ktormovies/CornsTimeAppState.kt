package com.enmanuelbergling.ktormovies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.MOVIES_GRAPH_ROUTE
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.navigateToMoviesGraph
import com.enmanuelbergling.ktormovies.ui.screen.series.navigation.navigateToSeriesGraph

@Composable
fun rememberCtiAppState(
    navController: NavHostController = rememberNavController()
) = CornsTimeAppState(navController)


@Stable
class CornsTimeAppState(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentRoute: String?
        @Composable get() = currentDestination?.route

    val startDestination = MOVIES_GRAPH_ROUTE

    val isTopDetination: Boolean
        @Composable get() = currentRoute in TopDestination.values().map { it.route }

    val shouldShowMainTopAppBar: Boolean
        @Composable get() = isTopDetination

    fun navigateToTopDestination(destination: TopDestination) {
        when (destination) {
            TopDestination.Movie -> navController.navigateToMoviesGraph(
                navOptions {
                    launchSingleTop = true
                    popUpTo(MOVIES_GRAPH_ROUTE) {

                    }
                }
            )
            TopDestination.Series -> navController.navigateToSeriesGraph(
                navOptions {
                    launchSingleTop = true
                    popUpTo(MOVIES_GRAPH_ROUTE) {

                    }
                }
            )
        }
    }
}