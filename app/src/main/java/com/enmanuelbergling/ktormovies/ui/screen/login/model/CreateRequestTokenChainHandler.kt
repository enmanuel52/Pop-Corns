package com.enmanuelbergling.ktormovies.ui.screen.login.model

import com.enmanuelbergling.ktormovies.domain.design.CannotHandleException
import com.enmanuelbergling.ktormovies.domain.design.ChainHandler
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.usecase.auth.CreateRequestTokenUC
import kotlinx.coroutines.flow.update

class CreateRequestTokenChainHandler(
    private val nextHandler: CreateSessionFromLoginChainHandler,
    private val createRequestTokenUC: CreateRequestTokenUC,
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