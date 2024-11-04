package com.enmanuelbergling.feature.series.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.enmanuelbergling.core.ui.components.topComposable
import com.enmanuelbergling.feature.series.home.SeriesScreen
import kotlinx.serialization.Serializable


@Serializable
data object SeriesGraphDestination

@Serializable
data object SeriesDestination

fun NavHostController.navigateToSeriesGraph(navOptions: NavOptions) {
    navigate(SeriesGraphDestination, navOptions)
}

fun NavGraphBuilder.seriesGraph(
    onOpenDrawer: () -> Unit,
) {
    navigation<SeriesGraphDestination>(startDestination = SeriesDestination) {
        topComposable<SeriesDestination> {
            SeriesScreen(onOpenDrawer)
        }
    }
}