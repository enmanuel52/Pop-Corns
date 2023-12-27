package com.enmanuelbergling.ktormovies.ui.screen.actor.details.model

import com.enmanuelbergling.ktormovies.domain.model.actor.ActorDetails
import com.enmanuelbergling.ktormovies.domain.model.actor.KnownMovie

data class ActorDetailsUiData(
    val details: ActorDetails? = null,
    val knownMovies: List<KnownMovie> = listOf(),
    val actorId: Int = 0,
) {
    val skipDetails get() = details != null
    val skipKnownMovies get() = knownMovies.isNotEmpty()
}
