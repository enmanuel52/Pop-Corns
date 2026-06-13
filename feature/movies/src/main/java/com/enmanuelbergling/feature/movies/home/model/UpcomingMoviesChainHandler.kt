package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.NewChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetUpcomingMoviesUC
import com.enmanuelbergling.core.model.core.ResultHandler

class UpcomingMoviesChainHandler(
    private val getUpcomingMoviesUC: GetUpcomingMoviesUC,
) : NewChainHandler<MoviesRequest> {
    override var nextChainHandler: NewChainHandler<MoviesRequest>? = null

    override suspend fun handle(request: MoviesRequest): MoviesRequest =
        if (request.upcoming.isNotEmpty()) request
        else when (val result = getUpcomingMoviesUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.apply {
                upcoming = result.data?.results.orEmpty().shuffled()
            }
        }
}
