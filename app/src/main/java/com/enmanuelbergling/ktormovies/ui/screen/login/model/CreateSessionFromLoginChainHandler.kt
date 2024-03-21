package com.enmanuelbergling.ktormovies.ui.screen.login.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.domain.usecase.auth.CreateSessionFromLoginUC

class CreateSessionFromLoginChainHandler(
    private val nextHandler: CreateSessionIdChainHandler,
    private val createSessionFromLoginUC: CreateSessionFromLoginUC,
) : ChainHandler<LoginChainState> {
    override val nextChainHandler: ChainHandler<LoginChainState>?
        get() = nextHandler

    override suspend fun handle(request: LoginChainState) =
        when (val result = createSessionFromLoginUC(
            request.value.toCreateSessionPost()
        )) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> Unit
        }
}