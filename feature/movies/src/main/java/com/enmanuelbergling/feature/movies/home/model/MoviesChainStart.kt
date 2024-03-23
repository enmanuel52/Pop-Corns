package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.domain.design.ChainHandler
import kotlinx.coroutines.flow.MutableStateFlow

class MoviesChainStart(
    private val upcomingMoviesHandler: UpcomingMoviesChainHandler,
) : ChainHandler<MutableStateFlow<MoviesUiData>> {
    override val nextChainHandler: ChainHandler<MutableStateFlow<MoviesUiData>>
        get() = upcomingMoviesHandler

    override suspend fun handle(request: MutableStateFlow<MoviesUiData>) = Unit
}