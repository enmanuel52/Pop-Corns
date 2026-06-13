package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetTopRatedMoviesUC
import com.enmanuelbergling.core.model.core.ResultHandler

class TopRatedMoviesChainHandler(
    private val getTopRatedMoviesUC: GetTopRatedMoviesUC,
) : ChainHandler<MoviesRequest> {
    override var nextChainHandler: ChainHandler<MoviesRequest>? = null

    override suspend fun handle(request: MoviesRequest): MoviesRequest =
        if (request.topRated.isNotEmpty()) request
        else when (val result = getTopRatedMoviesUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.apply {
                topRated = result.data?.results.orEmpty()
            }
        }
}
