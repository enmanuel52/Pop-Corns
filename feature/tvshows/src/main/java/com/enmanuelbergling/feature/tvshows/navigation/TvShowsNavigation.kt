package com.enmanuelbergling.feature.tvshows.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.Surface
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.enmanuelbergling.core.model.TvShowsSection
import com.enmanuelbergling.core.ui.components.topComposable
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.tvshows.favorite.FavoriteTvShowsRoute
import com.enmanuelbergling.feature.tvshows.home.TvShowsScreen
import com.enmanuelbergling.feature.tvshows.list.AiringTodayTvShowsScreen
import com.enmanuelbergling.feature.tvshows.list.OnTheAirTvShowsScreen
import com.enmanuelbergling.feature.tvshows.list.PopularTvShowsScreen
import com.enmanuelbergling.feature.tvshows.list.TopRatedTvShowsScreen
import kotlinx.serialization.Serializable


@Serializable
data object TvShowsGraphDestination

@Serializable
data object TvShowsDestination

@Serializable
data class TvShowsDetailFlowDestination(val tvShowId: Int)

@Serializable
data class TvShowsSectionDestination(val section: String)

@Serializable
data object FavoriteTvShowsDestination

fun NavHostController.navigateToTvShowsGraph(navOptions: NavOptions) {
    navigate(TvShowsGraphDestination, navOptions)
}

fun NavHostController.navigateToTvShowsDetailFlow(tvShowId: Int, navOptions: NavOptions? = null) {
    navigate(TvShowsDetailFlowDestination(tvShowId), navOptions)
}

fun NavHostController.navigateToTvShowsSection(
    section: TvShowsSection,
    navOptions: NavOptions? = null,
) {
    navigate(TvShowsSectionDestination("$section"), navOptions)
}

fun NavHostController.navigateToFavoriteTvShows(navOptions: NavOptions? = null) {
    navigate(FavoriteTvShowsDestination, navOptions)
}

fun NavGraphBuilder.tvShowsGraph(
    onTvShows: (id: Int) -> Unit,
    onSection: (TvShowsSection) -> Unit,
    onFavorites: () -> Unit,
    onActor: (ActorDetailNavAction) -> Unit,
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit,
) {
    navigation<TvShowsGraphDestination>(startDestination = TvShowsDestination) {
        topComposable<TvShowsDestination> {
            TvShowsScreen(
                onDetails = onTvShows,
                onMore = onSection,
                onFavorites = onFavorites,
                onOpenDrawer = onOpenDrawer,
            )
        }

        composable<TvShowsDetailFlowDestination> { backStackEntry ->
            val tvShowId = backStackEntry.toRoute<TvShowsDetailFlowDestination>().tvShowId
            Surface {
                SharedTransitionLayout {
                    TvShowsDetailNavDisplay(
                        tvShowId = tvShowId,
                        onActor = onActor,
                        onBack = onBack,
                    )
                }
            }
        }

        composable<TvShowsSectionDestination> { backStackEntry ->
            val stringSection = backStackEntry.toRoute<TvShowsSectionDestination>().section
            val sectionResult = runCatching { TvShowsSection.valueOf(stringSection) }
            sectionResult.onSuccess { section ->
                when (section) {
                    TvShowsSection.Popular -> PopularTvShowsScreen(
                        onTvShows = onTvShows,
                        onBack = onBack
                    )

                    TvShowsSection.TopRated -> TopRatedTvShowsScreen(
                        onTvShows = onTvShows,
                        onBack = onBack
                    )

                    TvShowsSection.OnTheAir -> OnTheAirTvShowsScreen(
                        onTvShows = onTvShows,
                        onBack = onBack
                    )

                    TvShowsSection.AiringToday -> AiringTodayTvShowsScreen(
                        onTvShows = onTvShows,
                        onBack = onBack
                    )
                }
            }.onFailure { onBack() }
        }

        composable<FavoriteTvShowsDestination> {
            FavoriteTvShowsRoute(onTvShowsDetails = onTvShows, onBack = onBack)
        }
    }
}
