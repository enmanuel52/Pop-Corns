package com.enmanuelbergling.ktormovies.ui.screen.actor.details.model

import com.enmanuelbergling.ktormovies.domain.design.CannotHandleException
import com.enmanuelbergling.ktormovies.domain.design.ChainHandler
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetMoviesByActorUC
import kotlinx.coroutines.flow.update

class ActorKnownMoviesChainHandler(
    private val getMoviesByActorUC: GetMoviesByActorUC,
) : ChainHandler<ActorDetailsUiState> {
    override val nextChainHandler: ChainHandler<ActorDetailsUiState>?
        get() = null

    override suspend fun handle(request: ActorDetailsUiState) =
        if (request.value.skipKnownMovies) Unit
        else when (val result = getMoviesByActorUC(request.value.actorId)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.update {
                it.copy(knownMovies = result.data.orEmpty())
            }
        }
}