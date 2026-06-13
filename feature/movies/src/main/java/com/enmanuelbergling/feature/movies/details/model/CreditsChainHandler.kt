package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieCreditsUC
import com.enmanuelbergling.core.model.core.ResultHandler

class CreditsChainHandler(
    private val getMovieCreditsUC: GetMovieCreditsUC,
) : ChainHandler<MovieDetailsChainRequest> {
    override var nextChainHandler: ChainHandler<MovieDetailsChainRequest>? = null

    override suspend fun handle(request: MovieDetailsChainRequest): MovieDetailsChainRequest {
        if (request.skipCredits) return request

        return when (val result = getMovieCreditsUC(request.movieId)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.apply { credits = result.data }
        }
    }
}
