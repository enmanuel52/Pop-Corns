package com.enmanuelbergling.feature.auth.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.NewChainHandler
import com.enmanuelbergling.core.domain.usecase.auth.CreateRequestTokenUC
import com.enmanuelbergling.core.model.core.ResultHandler

class CreateRequestTokenChainHandler(
    private val createRequestTokenUC: CreateRequestTokenUC,
) : NewChainHandler<LoginRequest> {

    override var nextChainHandler: NewChainHandler<LoginRequest>? = null

    override suspend fun handle(request: LoginRequest): LoginRequest =
        when (val result = createRequestTokenUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.copy(
                requestToken = result.data.orEmpty()
            )
        }
}
