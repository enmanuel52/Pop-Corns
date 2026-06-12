package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieAccountStatesUC
import com.enmanuelbergling.core.model.core.ResultHandler
import kotlinx.coroutines.flow.update

class MovieAccountStatesChainHandler(
    private val getMovieAccountStatesUC: GetMovieAccountStatesUC,
    private val sessionId: String,
) : ChainHandler<MovieDetailsUiState> {
    override val nextChainHandler: ChainHandler<MovieDetailsUiState>?
        get() = null

    override suspend fun handle(request: MovieDetailsUiState) {
        if (sessionId.isNotBlank()) {
            when (val result = getMovieAccountStatesUC(request.value.movieId, sessionId)) {
                is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
                is ResultHandler.Success -> request.update {
                    it.copy(accountStates = result.data)
                }
            }
        }
    }
}
