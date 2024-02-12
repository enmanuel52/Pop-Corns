package com.enmanuelbergling.ktormovies.ui.screen.watchlist.navigation

import com.enmanuelbergling.ktormovies.ui.screen.watchlist.details.WatchListDetailsRoute
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.home.WatchListRoute
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path

const val LIST_GRAPH_ROUTE = "list_graph_route"
const val LIST_SCREEN_ROUTE = "list_screen_route"
private const val LIST_DETAILS_SCREEN_ROUTE = "list_details_screen_route"

private const val LIST_ID_ARG = "list_id_arg"
private const val LIST_NAME_ARG = "list_name_arg"

fun Navigator.navigateToListGraph(navOptions: NavOptions? = null) {
    navigate(LIST_GRAPH_ROUTE, navOptions)
}

fun Navigator.navigateToListDetailsScreen(
    listId: Int,
    listName: String,
    navOptions: NavOptions? = null,
) {
    navigate("/$LIST_DETAILS_SCREEN_ROUTE/$listId/$listName", navOptions)
}

fun RouteBuilder.listGraph(
    onDetails: (listId: Int, listName: String) -> Unit,
    onMovieDetails: (movieId: Int) -> Unit,
    onBack: () -> Unit,
) {
    group(route = LIST_GRAPH_ROUTE, initialRoute = "/$LIST_SCREEN_ROUTE") {
        scene("/$LIST_SCREEN_ROUTE") {
            WatchListRoute(onDetails = onDetails, onBack = onBack)
        }

        scene("/$LIST_DETAILS_SCREEN_ROUTE/{$LIST_ID_ARG}/{$LIST_NAME_ARG}") {
            val listId = it.path(LIST_ID_ARG, 0)!!
            val listName = it.path(LIST_NAME_ARG, "Details")!!

            WatchListDetailsRoute(
                listId = listId,
                listName = listName, onMovieDetails = onMovieDetails,
                onBack = onBack
            )
        }
    }
}