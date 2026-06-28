package com.enmanuelbergling.feature.series.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.enmanuelbergling.core.model.SeriesSection
import com.enmanuelbergling.core.ui.components.topComposable
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.series.favorite.FavoriteSeriesRoute
import com.enmanuelbergling.feature.series.home.SeriesScreen
import com.enmanuelbergling.feature.series.list.AiringTodaySeriesScreen
import com.enmanuelbergling.feature.series.list.OnTheAirSeriesScreen
import com.enmanuelbergling.feature.series.list.PopularSeriesScreen
import com.enmanuelbergling.feature.series.list.TopRatedSeriesScreen
import com.enmanuelbergling.feature.series.watchlist.WatchlistSeriesRoute
import kotlinx.serialization.Serializable


@Serializable
data object SeriesGraphDestination

@Serializable
data object SeriesDestination

@Serializable
data class SeriesDetailFlowDestination(val seriesId: Int)

@Serializable
data class SeriesSectionDestination(val section: String)

@Serializable
data object FavoriteSeriesDestination

@Serializable
data object WatchlistSeriesDestination

fun NavHostController.navigateToSeriesGraph(navOptions: NavOptions) {
    navigate(SeriesGraphDestination, navOptions)
}

fun NavHostController.navigateToSeriesDetailFlow(seriesId: Int, navOptions: NavOptions? = null) {
    navigate(SeriesDetailFlowDestination(seriesId), navOptions)
}

fun NavHostController.navigateToSeriesSection(
    section: SeriesSection,
    navOptions: NavOptions? = null,
) {
    navigate(SeriesSectionDestination("$section"), navOptions)
}

fun NavHostController.navigateToFavoriteSeries(navOptions: NavOptions? = null) {
    navigate(FavoriteSeriesDestination, navOptions)
}

fun NavHostController.navigateToWatchlistSeries(navOptions: NavOptions? = null) {
    navigate(WatchlistSeriesDestination, navOptions)
}

fun NavGraphBuilder.seriesGraph(
    onSeries: (id: Int) -> Unit,
    onSection: (SeriesSection) -> Unit,
    onFavorites: () -> Unit,
    onWatchlist: () -> Unit,
    onActor: (ActorDetailNavAction) -> Unit,
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit,
) {
    navigation<SeriesGraphDestination>(startDestination = SeriesDestination) {
        topComposable<SeriesDestination> {
            SeriesScreen(
                onDetails = onSeries,
                onMore = onSection,
                onFavorites = onFavorites,
                onWatchlist = onWatchlist,
                onOpenDrawer = onOpenDrawer,
            )
        }

        composable<SeriesDetailFlowDestination> { backStackEntry ->
            val seriesId = backStackEntry.toRoute<SeriesDetailFlowDestination>().seriesId
            SeriesDetailNavDisplay(
                seriesId = seriesId,
                onActor = onActor,
                onBack = onBack,
            )
        }

        composable<SeriesSectionDestination> { backStackEntry ->
            val stringSection = backStackEntry.toRoute<SeriesSectionDestination>().section
            val sectionResult = runCatching { SeriesSection.valueOf(stringSection) }
            sectionResult.onSuccess { section ->
                when (section) {
                    SeriesSection.Popular -> PopularSeriesScreen(onSeries = onSeries, onBack = onBack)
                    SeriesSection.TopRated -> TopRatedSeriesScreen(onSeries = onSeries, onBack = onBack)
                    SeriesSection.OnTheAir -> OnTheAirSeriesScreen(onSeries = onSeries, onBack = onBack)
                    SeriesSection.AiringToday -> AiringTodaySeriesScreen(onSeries = onSeries, onBack = onBack)
                }
            }.onFailure { onBack() }
        }

        composable<FavoriteSeriesDestination> {
            FavoriteSeriesRoute(onSeriesDetails = onSeries, onBack = onBack)
        }

        composable<WatchlistSeriesDestination> {
            WatchlistSeriesRoute(onSeriesDetails = onSeries, onBack = onBack)
        }
    }
}
