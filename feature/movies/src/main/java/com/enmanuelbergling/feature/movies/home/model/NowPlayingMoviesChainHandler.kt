package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetNowPlayingMoviesUC
import com.enmanuelbergling.core.model.core.ResultHandler

class NowPlayingMoviesChainHandler(
    private val getNowPlayingMoviesUC: GetNowPlayingMoviesUC,
) : ChainHandler<MoviesRequest> {
    override var nextChainHandler: ChainHandler<MoviesRequest>? = null

    override suspend fun handle(request: MoviesRequest): MoviesRequest =
        if (request.nowPlaying.isNotEmpty()) request
        else when (val result = getNowPlayingMoviesUC()) {
            is ResultHandler.Error -> throw CannotHandleException(
                result.exception.message.orEmpty(),
                result.exception
            )
            is ResultHandler.Success -> request.apply {
                nowPlaying = result.data?.results.orEmpty()
            }
        }
}
