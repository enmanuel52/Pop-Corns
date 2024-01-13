package com.enmanuelbergling.ktormovies.ui.screen.movie.home.model

import com.enmanuelbergling.ktormovies.domain.design.CannotHandleException
import com.enmanuelbergling.ktormovies.domain.design.ChainHandler
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.usecase.GetUpcomingMoviesUC
import kotlinx.coroutines.flow.update

class UpcomingMoviesChainHandler(
    private val getUpcomingMoviesUC: GetUpcomingMoviesUC,
    private val next: TopRatedMoviesChainHandler,
) : ChainHandler<MoviesUiState> {
    override val nextChainHandler: ChainHandler<MoviesUiState>
        get() = next

    override suspend fun handle(request: MoviesUiState) =
        if (request.value.skipUpcoming) Unit
        else when (val result = getUpcomingMoviesUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.update {
                it.copy(upcoming = result.data.orEmpty())
            }
        }
}