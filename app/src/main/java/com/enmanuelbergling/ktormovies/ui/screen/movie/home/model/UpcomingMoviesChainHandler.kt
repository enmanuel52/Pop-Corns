package com.enmanuelbergling.ktormovies.ui.screen.movie.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetUpcomingMoviesUC
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