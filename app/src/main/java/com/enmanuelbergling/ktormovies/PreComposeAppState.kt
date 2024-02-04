package com.enmanuelbergling.ktormovies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.enmanuelbergling.ktormovies.domain.model.settings.DarkTheme
import com.enmanuelbergling.ktormovies.navigation.DrawerDestination
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import com.enmanuelbergling.ktormovies.ui.screen.actor.navigation.navigateToActorsGraph
import com.enmanuelbergling.ktormovies.ui.screen.login.navigation.LOGIN_ROUTE
import com.enmanuelbergling.ktormovies.ui.screen.login.navigation.navigateToLoginScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.MOVIES_GRAPH_ROUTE
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.navigateToMoviesGraph
import com.enmanuelbergling.ktormovies.ui.screen.series.navigation.navigateToSeriesGraph
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.BackStackEntry
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun rememberPreCtiAppState(
    isOnline: Boolean = true,
    darkTheme: DarkTheme = DarkTheme.System,
    navigator: Navigator = rememberNavigator(),
) = PreComposeAppState(isOnline, darkTheme, navigator)


@Stable
class PreComposeAppState(
    val isOnline: Boolean = true,
    val darkTheme: DarkTheme = DarkTheme.System,
    val navigator: Navigator,
) {
    private val backStackEntry: BackStackEntry?
        @Composable get() = navigator
            .currentEntry
            .collectAsStateWithLifecycle(initial = null)
            .value

    @Composable
    fun matchRoute(route: String) = backStackEntry?.hasRoute("/$route") ?: false

    val startDestination = LOGIN_ROUTE

    val isTopDestination: Boolean
        @Composable get() = DrawerDestination.entries.map { it.routes }.flatten()
            .any { route -> matchRoute(route) }

    val shouldShowMainBottomNav: Boolean
        @Composable get() = TopDestination.entries.map { it.route }
            .any { route -> matchRoute(route) }

    fun navigateToTopDestination(destination: TopDestination) {
        when (destination) {
            TopDestination.Movie -> navigator.navigateToMoviesGraph(
                NavOptions(
                    launchSingleTop = true,
                    popUpTo = PopUpTo(MOVIES_GRAPH_ROUTE, true)
                )
            )

            TopDestination.Series -> navigator.navigateToSeriesGraph(
                NavOptions(
                    launchSingleTop = true,
                    popUpTo = PopUpTo(MOVIES_GRAPH_ROUTE)
                )
            )
        }
    }

    /**
     * clear back stack before going to login
     * */
    fun navigateAfterLogout() {
        val navOptions = NavOptions(
            popUpTo = PopUpTo.First(inclusive = true)
        )
        navigator.navigateToLoginScreen(navOptions)
    }

    fun navigateToDrawerDestination(destination: DrawerDestination) {
        when (destination) {
            DrawerDestination.Home -> navigator.navigateToMoviesGraph(
                NavOptions(
                    launchSingleTop = true,
                    popUpTo = PopUpTo(MOVIES_GRAPH_ROUTE, true)
                )
            )

            DrawerDestination.Actor -> navigator.navigateToActorsGraph(
                NavOptions(
                    launchSingleTop = true
                )
            )
        }
    }
}