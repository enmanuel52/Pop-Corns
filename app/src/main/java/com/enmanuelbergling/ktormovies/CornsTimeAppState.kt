package com.enmanuelbergling.ktormovies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.enmanuelbergling.ktormovies.navigation.DrawerDestination
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import com.enmanuelbergling.ktormovies.ui.screen.actor.navigation.navigateToActorsGraph
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.MOVIES_GRAPH_ROUTE
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.navigateToMoviesGraph
import com.enmanuelbergling.ktormovies.ui.screen.series.navigation.navigateToSeriesGraph
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun rememberCtiAppState(
    navController: NavHostController = rememberNavController(),
    isOnline: Boolean = true,
    navigator: Navigator = rememberNavigator(),
) = CornsTimeAppState(navController, isOnline, navigator)


@Stable
class CornsTimeAppState(
    val navController: NavHostController,
    val isOnline: Boolean = true,
    val navigator: Navigator,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentRoute: String?
        @Composable get() = currentDestination?.route

    val startDestination = MOVIES_GRAPH_ROUTE

    val isTopDestination: Boolean
        @Composable get() = currentRoute in DrawerDestination.values().map { it.routes }.flatten()

    val shouldShowMainBottomNav: Boolean
        @Composable get() = currentRoute in TopDestination.values().map { it.route }

    fun navigateToTopDestination(destination: TopDestination) {
        when (destination) {
            TopDestination.Movie -> navController.navigateToMoviesGraph(
                navOptions {
                    launchSingleTop = true
                    popUpTo(MOVIES_GRAPH_ROUTE) {
                        inclusive = true
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

    fun navigateToDrawerDestination(destination: DrawerDestination) {
        when (destination) {
            DrawerDestination.Home -> navController.navigateToMoviesGraph(
                navOptions {
                    launchSingleTop = true
                    popUpTo(MOVIES_GRAPH_ROUTE){
                        inclusive=true
                    }
                }
            )

            DrawerDestination.Actor -> navController.navigateToActorsGraph(
                navOptions {
                    launchSingleTop = true
                }
            )
        }
    }
}