@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.enmanuelbergling.feature.tvshows.navigation

import androidx.compose.animation.SharedTransitionScope
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
import com.enmanuelbergling.feature.tvshows.R
import com.enmanuelbergling.feature.tvshows.episodedetails.EpisodeDetailsScreen
import com.enmanuelbergling.feature.tvshows.episodes.EpisodesScreen
import com.enmanuelbergling.feature.tvshows.seasons.SeasonsScreen

/**
 * Self-contained Navigation 3 flow for a single tvShows, hosted inside the global
 * Navigation 2 destination. Uses the adaptive list-detail scene strategy so that on
 * wide windows the episodes list (supporting pane) sits beside the episode details
 * (the main / primary display area), and collapses to single-pane navigation on phones.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun SharedTransitionScope.TvShowsDetailNavDisplay(
    tvShowId: Int,
    onActor: (ActorDetailNavAction) -> Unit,
    onBack: () -> Unit,
) {
    val backStack = rememberNavBackStack(SeasonsKey(tvShowId))
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
        sharedTransitionScope = this,
        entryProvider = entryProvider {
            entry<SeasonsKey>(
                metadata = listMetadata(
                    placeholder = if (backStack.size == 1) stringResource(R.string.episodes)
                    else stringResource(R.string.episode_details)
                )
            ) { key ->
                SeasonsScreen(
                    tvShowId = key.tvShowId,
                    onSeason = { seasonNumber ->
                        backStack.removeAll { it is EpisodesKey }
                        backStack.add(EpisodesKey(key.tvShowId, seasonNumber))
                    },
                    onBack = onBack,
                )
            }

            entry<EpisodesKey>(
                metadata = ListDetailSceneStrategy.extraPane()
            ) { key ->
                EpisodesScreen(
                    tvShowId = key.tvShowId,
                    seasonNumber = key.seasonNumber,
                    onEpisode = { episodeNumber ->
                        backStack.removeAll { it is EpisodeDetailsKey }
                        backStack.add(
                            EpisodeDetailsKey(key.tvShowId, key.seasonNumber, episodeNumber)
                        )
                    },
                    onBack = { backStack.removeAll { it !is SeasonsKey } },
                )
            }

            entry<EpisodeDetailsKey>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { key ->
                EpisodeDetailsScreen(
                    tvShowId = key.tvShowId,
                    seasonNumber = key.seasonNumber,
                    episodeNumber = key.episodeNumber,
                    onActor = onActor,
                    onBack = { backStack.removeLastOrNull() },
                )
            }
        }
    )
}

fun listMetadata(placeholder: String) = ListDetailSceneStrategy.listPane(
    detailPlaceholder = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
)
