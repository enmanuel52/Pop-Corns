package com.enmanuelbergling.ktormovies.ui.screen.login.model

import com.enmanuelbergling.ktormovies.domain.design.CannotHandleException
import com.enmanuelbergling.ktormovies.domain.design.ChainHandler
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.usecase.auth.CreateSessionIdUC

class CreateSessionIdChainHandler(
    private val createSessionIdUC: CreateSessionIdUC,
) : ChainHandler<LoginChainState> {
    override val nextChainHandler: ChainHandler<LoginChainState>?
        get() = null

    override suspend fun handle(request: LoginChainState) =
        when (val result = createSessionIdUC(request.value.requestToken)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> Unit
        }
}