package com.enmanuelbergling.feature.series.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.enmanuelbergling.feature.series.home.SeriesScreen
import kotlinx.serialization.Serializable


const val SERIES_SCREEN_ROUTE = "series_screen_route"

@Serializable
data object SeriesGraphDestination

@Serializable
data object SeriesDestination

fun NavHostController.navigateToSeriesGraph(navOptions: NavOptions) {
    navigate(SeriesGraphDestination, navOptions)
}

fun NavGraphBuilder.seriesGraph() {
    navigation<SeriesGraphDestination>(startDestination = SeriesDestination) {
        composable<SeriesDestination> {
            SeriesScreen()
        }
    }
}