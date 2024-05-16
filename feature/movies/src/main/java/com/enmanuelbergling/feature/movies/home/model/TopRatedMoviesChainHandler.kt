package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetTopRatedMoviesUC
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
                it.copy(topRated = result.data?.results.orEmpty())
            }
        }
}