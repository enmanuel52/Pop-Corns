package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.ChainHandler

/**
 * start a chain of call to get the details of a movie
 * */
class MovieDetailsChainStart(private val firstHandler: MovieDetailsChainHandler) :
    ChainHandler<MovieDetailsChainRequest> {
    override val nextChainHandler: ChainHandler<MovieDetailsChainRequest>
        get() = firstHandler

    override suspend fun handle(request: MovieDetailsChainRequest) = Unit
}
