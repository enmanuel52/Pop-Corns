package com.enmanuelbergling.feature.series.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.series.R
import com.enmanuelbergling.feature.series.episodedetails.EpisodeDetailsScreen
import com.enmanuelbergling.feature.series.episodes.EpisodesScreen
import com.enmanuelbergling.feature.series.seasons.SeasonsScreen

/**
 * Self-contained Navigation 3 flow for a single series, hosted inside the global
 * Navigation 2 destination. Uses the adaptive list-detail scene strategy so that on
 * wide windows the episodes list (supporting pane) sits beside the episode details
 * (the main / primary display area), and collapses to single-pane navigation on phones.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun SeriesDetailNavDisplay(
    seriesId: Int,
    onActor: (ActorDetailNavAction) -> Unit,
    onBack: () -> Unit,
) {
    val backStack = rememberNavBackStack(SeasonsKey(seriesId))
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size <= 1) {
                onBack()
            } else {
                backStack.removeLastOrNull()
            }
        },
        sceneStrategies = listOf(listDetailStrategy),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<SeasonsKey> { key ->
                SeasonsScreen(
                    seriesId = key.seriesId,
                    onSeason = { seasonNumber ->
                        backStack.add(EpisodesKey(key.seriesId, seasonNumber))
                    },
                    onBack = {
                        if (backStack.size <= 1) onBack() else backStack.removeLastOrNull()
                    },
                )
            }

            entry<EpisodesKey>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.episode_details),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                )
            ) { key ->
                EpisodesScreen(
                    seriesId = key.seriesId,
                    seasonNumber = key.seasonNumber,
                    onEpisode = { episodeNumber ->
                        backStack.add(
                            EpisodeDetailsKey(key.seriesId, key.seasonNumber, episodeNumber)
                        )
                    },
                    onBack = { backStack.removeLastOrNull() },
                )
            }

            entry<EpisodeDetailsKey>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { key ->
                EpisodeDetailsScreen(
                    seriesId = key.seriesId,
                    seasonNumber = key.seasonNumber,
                    episodeNumber = key.episodeNumber,
                    onActor = onActor,
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}
