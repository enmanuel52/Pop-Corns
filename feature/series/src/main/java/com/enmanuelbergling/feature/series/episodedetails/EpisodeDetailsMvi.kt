package com.enmanuelbergling.feature.series.episodedetails

import androidx.compose.runtime.Stable
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.EpisodeDetails
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction

@Stable
internal data class EpisodeDetailsState(
    val seriesId: Int,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val details: EpisodeDetails? = null,
    val uiState: SimplerUi = SimplerUi.Idle,
)

internal sealed interface EpisodeDetailsAction {
    data object OnBack : EpisodeDetailsAction
    data object OnRetry : EpisodeDetailsAction
    data class OnActorClick(val action: ActorDetailNavAction) : EpisodeDetailsAction
}

internal sealed interface EpisodeDetailsUiEvent {
    data object NavigateBack : EpisodeDetailsUiEvent
    data class NavigateToActor(val action: ActorDetailNavAction) : EpisodeDetailsUiEvent
}
