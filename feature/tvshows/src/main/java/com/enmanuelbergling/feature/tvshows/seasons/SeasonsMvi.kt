package com.enmanuelbergling.feature.tvshows.seasons

import androidx.compose.runtime.Stable
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.TvShowDetails

@Stable
internal data class SeasonsState(
    val tvShowId: Int,
    val details: TvShowDetails? = null,
    val uiState: SimplerUi = SimplerUi.Idle,
    val expandedSeasonId: Int? = null,
)

internal sealed interface SeasonsAction {
    data object OnBack : SeasonsAction
    data object OnRetry : SeasonsAction
    data class OnSeasonClick(val seasonNumber: Int) : SeasonsAction
    data class OnSeasonLongClick(val seasonId: Int) : SeasonsAction
}

internal sealed interface SeasonsEvent {
    data object NavigateBack : SeasonsEvent
    data class NavigateToEpisodes(val seasonNumber: Int) : SeasonsEvent
}
