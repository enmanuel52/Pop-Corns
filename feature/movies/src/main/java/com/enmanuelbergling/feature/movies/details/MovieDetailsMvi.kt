package com.enmanuelbergling.feature.movies.details

import androidx.compose.runtime.Stable
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.MovieAccountStates
import com.enmanuelbergling.core.model.movie.MovieCredits
import com.enmanuelbergling.core.model.movie.MovieDetails
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsChainRequest

@Stable
data class MovieDetailsState(
    val details: MovieDetails? = null,
    val credits: MovieCredits? = null,
    val accountStates: MovieAccountStates? = null,
    val movieId: Int,
    val isWatchlistLoading: Boolean = false,
    val uiState: SimplerUi = SimplerUi.Idle,
)

fun MovieDetailsState.toChainRequest() = MovieDetailsChainRequest(
    movieId = movieId,
    details = details,
    credits = credits,
    accountStates = accountStates
)

sealed interface MovieDetailsAction {
    data object OnBack : MovieDetailsAction
    data object OnRetry : MovieDetailsAction
    data object OnWatchlistClick : MovieDetailsAction
    data class OnActorClick(val action: ActorDetailNavAction) : MovieDetailsAction
}

sealed interface MovieDetailsEvent {
    data object NavigateBack : MovieDetailsEvent
    data class NavigateToActor(val action: ActorDetailNavAction) : MovieDetailsEvent
}
