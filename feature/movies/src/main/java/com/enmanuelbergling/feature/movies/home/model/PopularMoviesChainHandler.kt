package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetPopularMoviesUC
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
                it.copy(popular = result.data?.results.orEmpty())
            }
        }
}