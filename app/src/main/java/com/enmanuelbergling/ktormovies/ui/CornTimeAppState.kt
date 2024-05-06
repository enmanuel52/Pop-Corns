package com.enmanuelbergling.ktormovies.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.enmanuelbergling.core.model.settings.DarkTheme
import com.enmanuelbergling.feature.actor.navigation.navigateToActorsGraph
import com.enmanuelbergling.feature.auth.navigation.navigateToLoginScreen
import com.enmanuelbergling.feature.movies.navigation.MOVIES_GRAPH_ROUTE
import com.enmanuelbergling.feature.movies.navigation.MoviesGraphDestination
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesGraph
import com.enmanuelbergling.feature.series.navigation.navigateToSeriesGraph
import com.enmanuelbergling.feature.watchlists.navigation.navigateToListGraph
import com.enmanuelbergling.ktormovies.navigation.DrawerDestination
import com.enmanuelbergling.ktormovies.navigation.TopDestination

@Composable
fun rememberCornTimeAppState(
    isOnline: Boolean = true,
    darkTheme: DarkTheme = DarkTheme.System,
    navController: NavHostController = rememberNavController(),
) = remember(navController) { CornTimeAppState(isOnline, darkTheme, navController) }


@Stable
class CornTimeAppState(
    val isOnline: Boolean = true,
    val darkTheme: DarkTheme = DarkTheme.System,
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = MoviesGraphDestination

    val isTopDestination: Boolean
        @Composable get() = TopDestination.entries.map { it.route }
            .any { route -> currentDestination?.hasRoute(route::class) == true }

    @Composable
    fun matchRoute(route: Any) = currentDestination?.hasRoute(route::class) == true

    val shouldShowMainBottomNav: Boolean
        @Composable get() = isTopDestination

    fun navigateToTopDestination(destination: TopDestination) {
        when (destination) {
            TopDestination.Movies -> navController.navigateToMoviesGraph(
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
                        inclusive = false
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
                    popUpTo(MOVIES_GRAPH_ROUTE) {
                        inclusive = true
                    }
                }
            )

            DrawerDestination.Actors -> navController.navigateToActorsGraph(
                navOptions {
                    launchSingleTop = true
                }
            )

            DrawerDestination.Lists -> navController.navigateToListGraph(
                navOptions {
                    launchSingleTop = true
                }
            )
        }
    }

    fun navigateToLogin() = navController.navigateToLoginScreen(
        navOptions {
            launchSingleTop = true
            popUpTo(MOVIES_GRAPH_ROUTE) {
                inclusive = true
            }
        }
    )
}