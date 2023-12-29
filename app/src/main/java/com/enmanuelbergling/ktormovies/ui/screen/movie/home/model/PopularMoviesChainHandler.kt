package com.enmanuelbergling.ktormovies.ui.screen.movie.home.model

import com.enmanuelbergling.ktormovies.domain.design.CannotHandleException
import com.enmanuelbergling.ktormovies.domain.design.ChainHandler
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.usecase.GetPopularMoviesUC
import kotlinx.coroutines.flow.update

class PopularMoviesChainHandler(
    private val getPopularMoviesUC: GetPopularMoviesUC,
) : ChainHandler<MoviesUiState> {
    override val nextChainHandler: ChainHandler<MoviesUiState>?
        get() = null

    override suspend fun handle(request: MoviesUiState) =
        if (request.value.skipPopular) Unit
        else when (val result = getPopularMoviesUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.update {
                it.copy(popular = result.data.orEmpty())
            }
        }
}