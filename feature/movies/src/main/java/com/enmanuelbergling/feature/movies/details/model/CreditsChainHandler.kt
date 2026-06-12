package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.NewChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieCreditsUC
import com.enmanuelbergling.core.model.core.ResultHandler

class CreditsChainHandler(
    private val getMovieCreditsUC: GetMovieCreditsUC,
) : NewChainHandler<MovieDetailsChainRequest> {
    override var nextChainHandler: NewChainHandler<MovieDetailsChainRequest>? = null

    override suspend fun handle(request: MovieDetailsChainRequest): MovieDetailsChainRequest {
        if (request.skipCredits) return request

        return when (val result = getMovieCreditsUC(request.movieId)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.apply { credits = result.data }
        }
    }
}
