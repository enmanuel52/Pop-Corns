package com.enmanuelbergling.feature.watchlists.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.enmanuelbergling.core.ui.components.topComposable
import com.enmanuelbergling.core.ui.model.WatchlistShortcut
import com.enmanuelbergling.feature.watchlists.details.WatchListDetailsRoute
import com.enmanuelbergling.feature.watchlists.home.WatchListRoute
import kotlinx.serialization.Serializable

@Serializable
data object ListGraphDestination

@Serializable
data object WatchListDestination

@Serializable
data class ListDetailsDestination(
    val listId: Int,
    val listName: String,
)

fun NavHostController.navigateToListGraph(navOptions: NavOptions? = null) {
    navigate(ListGraphDestination, navOptions)
}

fun NavHostController.navigateToListDetailsScreen(
    listId: Int,
    listName: String,
    navOptions: NavOptions? = null,
) {
    navigate(ListDetailsDestination(listId, listName), navOptions)
}

fun NavGraphBuilder.listGraph(
    onDetails: (listId: Int, listName: String) -> Unit,
    onMovieDetails: (movieId: Int) -> Unit,
    onAddShortcut: (WatchlistShortcut) -> Unit,
    onDeleteShortcut: (watchlistId: Int) -> Unit,
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit,
) {
    navigation<ListGraphDestination>(startDestination = WatchListDestination) {
        topComposable<WatchListDestination> {
            WatchListRoute(
                onDetails = onDetails,
                onOpenDrawer = onOpenDrawer,
            )
        }

        composable<ListDetailsDestination> { backStackEntry ->
            val (listId, listName) = backStackEntry.toRoute<ListDetailsDestination>()

            WatchListDetailsRoute(
                listId = listId,
                listName = listName,
                onMovieDetails = onMovieDetails,
                onAddShortcut = onAddShortcut,
                onDeleteShortcut = onDeleteShortcut,
                onBack = onBack
            )
        }
    }
}