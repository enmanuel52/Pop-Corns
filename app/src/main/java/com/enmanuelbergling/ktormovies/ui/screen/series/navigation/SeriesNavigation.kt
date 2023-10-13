package com.enmanuelbergling.ktormovies.ui.screen.series.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.enmanuelbergling.ktormovies.ui.screen.series.home.SeriesScreen

const val SERIES_GRAPH_ROUTE = "series_graph_route"

const val SERIES_SCREEN_ROUTE = "series_screen_route"

fun NavHostController.navigateToSeriesGraph(navOptions: NavOptions?=null){
    navigate(SERIES_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.seriesGraph() {
    navigation(startDestination = SERIES_SCREEN_ROUTE, route = SERIES_GRAPH_ROUTE) {
        composable(SERIES_SCREEN_ROUTE) {
            SeriesScreen()
        }
    }
}