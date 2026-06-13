package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetPopularMoviesUC
import com.enmanuelbergling.core.model.core.ResultHandler

class PopularMoviesChainHandler(
    private val getPopularMoviesUC: GetPopularMoviesUC,
) : ChainHandler<MoviesRequest> {
    override var nextChainHandler: ChainHandler<MoviesRequest>? = null

    override suspend fun handle(request: MoviesRequest): MoviesRequest =
        if (request.popular.isNotEmpty()) request
        else when (val result = getPopularMoviesUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.apply {
                popular = result.data?.results.orEmpty()
            }
        }
}
