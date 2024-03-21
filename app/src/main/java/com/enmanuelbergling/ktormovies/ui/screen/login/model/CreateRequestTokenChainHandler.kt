package com.enmanuelbergling.ktormovies.ui.screen.login.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.domain.usecase.auth.CreateRequestTokenUC
import kotlinx.coroutines.flow.update

class CreateRequestTokenChainHandler(
    private val nextHandler: CreateSessionFromLoginChainHandler,
    private val createRequestTokenUC: com.enmanuelbergling.core.domain.usecase.auth.CreateRequestTokenUC,
) : ChainHandler<LoginChainState> {
    override val nextChainHandler: ChainHandler<LoginChainState>
        get() = nextHandler

    override suspend fun handle(request: LoginChainState) =
        when (val result = createRequestTokenUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.update {
                it.copy(
                    requestToken = result.data.orEmpty()
                )
            }
        }
}