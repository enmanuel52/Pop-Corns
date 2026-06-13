package com.enmanuelbergling.feature.actor.details

import com.enmanuelbergling.core.model.actor.ActorDetails
import com.enmanuelbergling.core.model.actor.KnownMovie
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsRequest

data class ActorDetailsState(
    val actorId: Int,
    val details: ActorDetails? = null,
    val knownMovies: List<KnownMovie> = listOf(),
    val uiState: SimplerUi = SimplerUi.Idle,
)

fun ActorDetailsState.toRequest() = ActorDetailsRequest(
    actorId = actorId,
    details = details,
    knownMovies = knownMovies
)

sealed interface ActorDetailsAction {
    data object OnBack : ActorDetailsAction
    data object OnRetry : ActorDetailsAction
    data class OnMovieClick(val movieId: Int) : ActorDetailsAction
}

sealed interface ActorDetailsEvent {
    data object NavigateBack : ActorDetailsEvent
    data class NavigateToMovie(val movieId: Int) : ActorDetailsEvent
}
