package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieAccountStatesUC
import com.enmanuelbergling.core.model.core.ResultHandler

class AccountStatesChainHandler(
    private val getMovieAccountStatesUC: GetMovieAccountStatesUC,
) : ChainHandler<MovieDetailsChainRequest> {
    override var nextChainHandler: ChainHandler<MovieDetailsChainRequest>? = null

    override suspend fun handle(request: MovieDetailsChainRequest): MovieDetailsChainRequest {
        when (val result = getMovieAccountStatesUC(request.movieId)) {
            is ResultHandler.Error -> Unit
            is ResultHandler.Success -> request.accountStates = result.data
        }

        return request
    }
}
