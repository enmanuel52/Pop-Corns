package com.enmanuelbergling.feature.auth.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.auth.CreateRequestTokenUC
import com.enmanuelbergling.core.model.core.ResultHandler

class CreateRequestTokenChainHandler(
    private val createRequestTokenUC: CreateRequestTokenUC,
) : ChainHandler<LoginRequest> {

    override var nextChainHandler: ChainHandler<LoginRequest>? = null

    override suspend fun handle(request: LoginRequest): LoginRequest =
        when (val result = createRequestTokenUC()) {
            is ResultHandler.Error -> throw CannotHandleException(
                result.exception.message.orEmpty(),
                result.exception
            )
            is ResultHandler.Success -> request.copy(
                requestToken = result.data.orEmpty()
            )
        }
}
