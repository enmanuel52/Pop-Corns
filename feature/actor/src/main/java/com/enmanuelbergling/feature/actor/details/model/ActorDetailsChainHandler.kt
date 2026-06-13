package com.enmanuelbergling.feature.actor.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetActorDetailsUC
import com.enmanuelbergling.core.model.core.ResultHandler

class ActorDetailsChainHandler(
    private val getActorDetailsUC: GetActorDetailsUC,
) : ChainHandler<ActorDetailsRequest> {
    override var nextChainHandler: ChainHandler<ActorDetailsRequest>? = null

    override suspend fun handle(request: ActorDetailsRequest): ActorDetailsRequest =
        if (request.details != null) request
        else when (val result = getActorDetailsUC(request.actorId)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.apply { details = result.data }
        }
}
