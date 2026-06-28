package com.enmanuelbergling.feature.series.episodes

import androidx.compose.runtime.Stable
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.SeasonDetails

@Stable
internal data class EpisodesState(
    val seriesId: Int,
    val seasonNumber: Int,
    val seasonDetails: SeasonDetails? = null,
    val uiState: SimplerUi = SimplerUi.Idle,
)

internal sealed interface EpisodesAction {
    data object OnBack : EpisodesAction
    data object OnRetry : EpisodesAction
    data class OnEpisodeClick(val episodeNumber: Int) : EpisodesAction
}

internal sealed interface EpisodesEvent {
    data object NavigateBack : EpisodesEvent
    data class NavigateToEpisodeDetails(val episodeNumber: Int) : EpisodesEvent
}
