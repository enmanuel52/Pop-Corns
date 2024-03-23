package com.enmanuelbergling.feature.actor.details.model

import com.enmanuelbergling.core.domain.design.ChainHandler

/**
 * start a chain of call to get the details of a movie
 * */
class ActorDetailsChainStart(
    private val firstHandler: ActorDetailsChainHandler,
) : ChainHandler<ActorDetailsUiState> {
    override val nextChainHandler: ChainHandler<ActorDetailsUiState>
        get() = firstHandler

    override suspend fun handle(request: ActorDetailsUiState) = Unit
}