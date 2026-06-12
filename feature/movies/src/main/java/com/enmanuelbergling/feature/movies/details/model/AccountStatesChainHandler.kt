package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.NewChainHandler
import com.enmanuelbergling.core.domain.usecase.auth.GetSavedSessionIdUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieAccountStatesUC
import com.enmanuelbergling.core.model.core.ResultHandler
import kotlinx.coroutines.flow.first

class AccountStatesChainHandler(
    private val getMovieAccountStatesUC: GetMovieAccountStatesUC,
    private val getSavedSessionIdUC: GetSavedSessionIdUC,
) : NewChainHandler<MovieDetailsChainRequest> {
    override var nextChainHandler: NewChainHandler<MovieDetailsChainRequest>? = null

    override suspend fun handle(request: MovieDetailsChainRequest): MovieDetailsChainRequest {
        val sessionId = getSavedSessionIdUC().first()

        if (sessionId.isNotBlank()) {
            when (val result = getMovieAccountStatesUC(request.movieId, sessionId)) {
                is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
                is ResultHandler.Success -> request.accountStates = result.data
            }
        }

        return request
    }
}
