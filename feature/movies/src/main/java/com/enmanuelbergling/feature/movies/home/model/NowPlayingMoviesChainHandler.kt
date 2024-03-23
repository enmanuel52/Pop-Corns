package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetNowPlayingMoviesUC
import kotlinx.coroutines.flow.update

class NowPlayingMoviesChainHandler(
    private val getNowPlayingMoviesUC: GetNowPlayingMoviesUC,
    private val next: PopularMoviesChainHandler,
) : ChainHandler<MoviesUiState> {
    override val nextChainHandler: ChainHandler<MoviesUiState>?
        get() = next

    override suspend fun handle(request: MoviesUiState) =
        if (request.value.skipNowPlaying) Unit
        else when (val result = getNowPlayingMoviesUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.update {
                it.copy(nowPlaying = result.data.orEmpty())
            }
        }
}