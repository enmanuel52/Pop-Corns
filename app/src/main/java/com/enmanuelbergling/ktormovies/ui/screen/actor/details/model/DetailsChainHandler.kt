package com.enmanuelbergling.ktormovies.ui.screen.actor.details.model

import com.enmanuelbergling.ktormovies.domain.design.ChainHandler

/**
 * start a chain of call to get the details of a movie
 * */
class DetailsChainHandler(
    private val firstHandler: ActorDetailsChainHandler,
) : ChainHandler<ActorDetailsUiState> {
    override val nextChainHandler: ChainHandler<ActorDetailsUiState>?
        get() = firstHandler

    override suspend fun handle(request: ActorDetailsUiState) = Unit
}