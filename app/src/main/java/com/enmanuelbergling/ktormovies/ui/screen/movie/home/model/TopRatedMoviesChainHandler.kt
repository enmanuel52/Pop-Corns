package com.enmanuelbergling.ktormovies.ui.screen.movie.home.model

import com.enmanuelbergling.ktormovies.domain.design.CannotHandleException
import com.enmanuelbergling.ktormovies.domain.design.ChainHandler
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.usecase.GetTopRatedMoviesUC
import kotlinx.coroutines.flow.update

class TopRatedMoviesChainHandler(
    private val getTopRatedMoviesUC: GetTopRatedMoviesUC,
    private val next: NowPlayingMoviesChainHandler,
) : ChainHandler<MoviesUiState> {
    override val nextChainHandler: ChainHandler<MoviesUiState>
        get() = next

    override suspend fun handle(request: MoviesUiState) =
        if (request.value.skipTopRated) Unit
        else when (val result = getTopRatedMoviesUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.update {
                it.copy(topRated = result.data.orEmpty())
            }
        }
}