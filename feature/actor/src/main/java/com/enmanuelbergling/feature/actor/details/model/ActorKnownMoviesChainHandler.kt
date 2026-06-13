package com.enmanuelbergling.feature.actor.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.NewChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetMoviesByActorUC
import com.enmanuelbergling.core.model.core.ResultHandler

class ActorKnownMoviesChainHandler(
    private val getMoviesByActorUC: GetMoviesByActorUC,
) : NewChainHandler<ActorDetailsRequest> {
    override var nextChainHandler: NewChainHandler<ActorDetailsRequest>? = null

    override suspend fun handle(request: ActorDetailsRequest): ActorDetailsRequest =
        if (request.knownMovies.isNotEmpty()) request
        else when (val result = getMoviesByActorUC(request.actorId)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.apply { knownMovies = result.data.orEmpty() }
        }
}
