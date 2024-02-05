package com.enmanuelbergling.ktormovies.ui.screen.list.navigation

import com.enmanuelbergling.ktormovies.ui.screen.list.ListScreen
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder

const val LIST_GRAPH_ROUTE = "list_graph_route"
const val LIST_SCREEN_ROUTE = "list_screen_route"

fun Navigator.navigateToListGraph(navOptions: NavOptions) {
    navigate(LIST_GRAPH_ROUTE, navOptions)
}

fun RouteBuilder.listGraph(

) {
    group(route = LIST_GRAPH_ROUTE, initialRoute = "/$LIST_SCREEN_ROUTE") {
        scene("/$LIST_SCREEN_ROUTE") {
            ListScreen()
        }
    }
}