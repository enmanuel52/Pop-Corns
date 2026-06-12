package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieAccountStatesUC
import com.enmanuelbergling.core.model.core.ResultHandler

class AccountStatesChainHandler(
    private val getMovieAccountStatesUC: GetMovieAccountStatesUC,
    private val sessionId: String,
) : ChainHandler<MovieDetailsChainRequest> {
    override val nextChainHandler: ChainHandler<MovieDetailsChainRequest>?
        get() = null

    override suspend fun handle(request: MovieDetailsChainRequest) {
        if (sessionId.isNotBlank()) {
            when (val result = getMovieAccountStatesUC(request.movieId, sessionId)) {
                is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
                is ResultHandler.Success -> request.accountStates = result.data
            }
        }
    }
}
