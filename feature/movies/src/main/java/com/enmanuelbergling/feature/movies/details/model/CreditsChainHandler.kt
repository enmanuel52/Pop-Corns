package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieCreditsUC
import kotlinx.coroutines.flow.update

class CreditsChainHandler(private val getMovieCreditsUC: GetMovieCreditsUC) :
    ChainHandler<MovieDetailsUiState> {
    override val nextChainHandler: ChainHandler<MovieDetailsUiState>?
        get() = null

    override suspend fun handle(request: MovieDetailsUiState) =
        if (request.value.skipCredits) Unit
        else when (val result = getMovieCreditsUC(request.value.movieId)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.update {
                it.copy(credits = result.data)
            }
        }
}