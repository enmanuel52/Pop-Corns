package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.ChainHandler

/**
 * start a chain of call to get the details of a movie
 * */
class MovieDetailsChainStart(private val firstHandler: MovieDetailsChainHandler) :
    ChainHandler<MovieDetailsUiState> {
    override val nextChainHandler: ChainHandler<MovieDetailsUiState>
        get() = firstHandler

    override suspend fun handle(request: MovieDetailsUiState) = Unit
}