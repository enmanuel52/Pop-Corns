package com.enmanuelbergling.ktormovies.ui.screen.login.model

import com.enmanuelbergling.ktormovies.domain.design.CannotHandleException
import com.enmanuelbergling.ktormovies.domain.design.ChainHandler
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.usecase.user.GetUserDetailsUC

class GetUserDetailsChainHandler(
    private val getUserDetailsUC: GetUserDetailsUC
): ChainHandler<LoginChainState> {

    override val nextChainHandler: ChainHandler<LoginChainState>?
        get() = null

    override suspend fun handle(request: LoginChainState) =
        when (val result = getUserDetailsUC()) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> Unit
        }
}