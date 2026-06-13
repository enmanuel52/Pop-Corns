package com.enmanuelbergling.feature.actor.details.model

data class ActorDetailsChain(
    val detailsHandler: ActorDetailsChainHandler,
    val knownMoviesHandler: ActorKnownMoviesChainHandler,
)
