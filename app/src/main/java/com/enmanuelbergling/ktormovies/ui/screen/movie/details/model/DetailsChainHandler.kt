package com.enmanuelbergling.ktormovies.ui.screen.movie.details.model

import com.enmanuelbergling.ktormovies.domain.design.ChainHandler

/**
 * start a chain of call to get the details of a movie
 * */
class DetailsChainHandler(private val firstHandler: MovieDetailsChainHandler) :
    ChainHandler<MovieDetailsUiState> {
    override val nextChainHandler: ChainHandler<MovieDetailsUiState>
        get() = firstHandler

    override suspend fun handle(request: MovieDetailsUiState) = Unit
}