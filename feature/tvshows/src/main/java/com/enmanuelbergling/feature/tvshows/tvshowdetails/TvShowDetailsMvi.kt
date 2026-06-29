package com.enmanuelbergling.feature.tvshows.tvshowdetails

import androidx.compose.runtime.Stable
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.TvAccountStates
import com.enmanuelbergling.core.model.tv.TvCredits
import com.enmanuelbergling.core.model.tv.TvShowDetails
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.tvshows.tvshowdetails.model.TvShowDetailsChainRequest

@Stable
internal data class TvShowDetailsState(
    val tvShowId: Int,
    val details: TvShowDetails? = null,
    val credits: TvCredits? = null,
    val accountStates: TvAccountStates? = null,
    val isWatchlistLoading: Boolean = false,
    val isFavoriteLoading: Boolean = false,
    val uiState: SimplerUi = SimplerUi.Idle,
)

internal fun TvShowDetailsState.toChainRequest() = TvShowDetailsChainRequest(
    tvShowId = tvShowId,
    details = details,
    credits = credits,
    accountStates = accountStates,
)

internal sealed interface TvShowDetailsAction {
    data object OnBack : TvShowDetailsAction
    data object OnRetry : TvShowDetailsAction
    data object OnWatchlistClick : TvShowDetailsAction
    data object OnFavoriteClick : TvShowDetailsAction
    data object OnSeeAllSeasons : TvShowDetailsAction
    data class OnSeasonClick(val seasonNumber: Int) : TvShowDetailsAction
    data class OnActorClick(val action: ActorDetailNavAction) : TvShowDetailsAction
}

internal sealed interface TvShowDetailsEvent {
    data object NavigateBack : TvShowDetailsEvent
    data object NavigateToSeasons : TvShowDetailsEvent
    data class NavigateToEpisodes(val seasonNumber: Int) : TvShowDetailsEvent
    data class NavigateToActor(val action: ActorDetailNavAction) : TvShowDetailsEvent
}
