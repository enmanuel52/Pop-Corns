package com.enmanuelbergling.feature.auth.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.auth.CreateSessionFromLoginUC
import com.enmanuelbergling.core.model.core.ResultHandler

class CreateSessionFromLoginChainHandler(
    private val createSessionFromLoginUC: CreateSessionFromLoginUC,
) : ChainHandler<LoginRequest> {

    override var nextChainHandler: ChainHandler<LoginRequest>? = null

    override suspend fun handle(request: LoginRequest): LoginRequest =
        when (val result = createSessionFromLoginUC(request.toCreateSessionPost())) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request
        }
}
