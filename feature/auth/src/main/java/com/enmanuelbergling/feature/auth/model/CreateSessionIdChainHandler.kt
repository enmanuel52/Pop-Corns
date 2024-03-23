package com.enmanuelbergling.feature.auth.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.auth.CreateSessionIdUC
import com.enmanuelbergling.core.model.core.ResultHandler

class CreateSessionIdChainHandler(
    private val createSessionIdUC: CreateSessionIdUC,
    private val nextHandler: GetUserDetailsChainHandler,
) : ChainHandler<LoginChainState> {
    override val nextChainHandler: ChainHandler<LoginChainState>
        get() = nextHandler

    override suspend fun handle(request: LoginChainState) =
        when (val result = createSessionIdUC(request.value.requestToken)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> Unit
        }
}