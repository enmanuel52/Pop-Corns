package com.enmanuelbergling.ktormovies.ui

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.enmanuelbergling.core.common.android_util.ShortCutModel
import com.enmanuelbergling.core.common.android_util.addDynamicShortCut
import com.enmanuelbergling.core.common.android_util.removeDynamicShortCut
import com.enmanuelbergling.core.ui.model.WatchlistShortcut
import com.enmanuelbergling.core.ui.util.watchlistShortcutId
import com.enmanuelbergling.feature.actor.navigation.navigateToActorsGraph
import com.enmanuelbergling.feature.movies.navigation.MoviesGraphDestination
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesGraph
import com.enmanuelbergling.feature.series.navigation.navigateToSeriesGraph
import com.enmanuelbergling.feature.settings.navigation.navigateToSettingsGraph
import com.enmanuelbergling.feature.watchlists.navigation.navigateToListGraph
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.navigation.DrawerDestination
import com.enmanuelbergling.ktormovies.navigation.TopDestination

@Composable
fun rememberCornTimeAppState(
    isOnline: Boolean = true,
    navController: NavHostController = rememberNavController(),
) = remember(navController) { CornTimeAppState(isOnline, navController) }


@Stable
class CornTimeAppState(
    val isOnline: Boolean = true,
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
                    popUpTo(MoviesGraphDestination) {
                        inclusive = true
                    }
                }
            )

            TopDestination.Series -> navController.navigateToSeriesGraph(
                navOptions {
                    launchSingleTop = true
                }
            )
        }
    }

    fun navigateToDrawerDestination(destination: DrawerDestination) {
        when (destination) {
            DrawerDestination.Home -> navController.navigateToMoviesGraph(
                navOptions {
                    launchSingleTop = true
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

            DrawerDestination.Settings -> navController.navigateToSettingsGraph(
                navOptions {
                    launchSingleTop = true
                }
            )
        }
    }

    fun addWatchlistShortcut(context: Context, watchlist: WatchlistShortcut) =
        context addWatchlistShortcut watchlist

    fun deleteWatchlistShortcut(context: Context, watchlistId: Int) =
        context removeDynamicShortCut watchlistShortcutId(watchlistId)
}

infix fun Context.addWatchlistShortcut(
    model: WatchlistShortcut,
) {
    addDynamicShortCut(
        ShortCutModel(
            id = watchlistShortcutId(model.id),
            shortLabel = model.name,
            iconRes = R.drawable.bookmark_icon,
            intent = Intent(this, CornsTimeActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                putExtra(getString(R.string.watch_list_id_extra), model.id)
                putExtra(getString(R.string.watch_list_name_extra), model.name)
            },
        )
    )
}