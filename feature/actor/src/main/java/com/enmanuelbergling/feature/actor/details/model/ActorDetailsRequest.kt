package com.enmanuelbergling.feature.actor.details.model

import com.enmanuelbergling.core.model.actor.ActorDetails
import com.enmanuelbergling.core.model.actor.KnownMovie

data class ActorDetailsRequest(
    val actorId: Int,
    var details: ActorDetails? = null,
    var knownMovies: List<KnownMovie> = listOf(),
)
