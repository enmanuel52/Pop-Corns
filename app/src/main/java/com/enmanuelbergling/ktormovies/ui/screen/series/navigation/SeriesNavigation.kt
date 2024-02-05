package com.enmanuelbergling.ktormovies.ui.screen.series.navigation

import com.enmanuelbergling.ktormovies.ui.screen.series.home.SeriesScreen
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

const val SERIES_GRAPH_ROUTE = "series_graph_route"

const val SERIES_SCREEN_ROUTE = "series_screen_route"

fun Navigator.navigateToSeriesGraph(navOptions: NavOptions) {
    navigate(SERIES_GRAPH_ROUTE, navOptions)
}

fun RouteBuilder.seriesGraph() {
    group(SERIES_GRAPH_ROUTE, "/$SERIES_SCREEN_ROUTE") {
        scene("/$SERIES_SCREEN_ROUTE", navTransition = NavTransition()) {
            SeriesScreen()
        }
    }
}