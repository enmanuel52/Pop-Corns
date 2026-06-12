package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.NewChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieDetailsUC
import com.enmanuelbergling.core.model.core.ResultHandler

class MovieDetailsChainHandler(
    private val getMovieDetailsUC: GetMovieDetailsUC,
) : NewChainHandler<MovieDetailsChainRequest> {
    override var nextChainHandler: NewChainHandler<MovieDetailsChainRequest>? = null

    override suspend fun handle(request: MovieDetailsChainRequest): MovieDetailsChainRequest {
        if (request.skipDetails) return request

        return when (val result = getMovieDetailsUC(request.movieId)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.apply { details = result.data }
        }
    }
}
