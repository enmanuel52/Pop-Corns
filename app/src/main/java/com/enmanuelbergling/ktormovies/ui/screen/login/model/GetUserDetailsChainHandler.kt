package com.enmanuelbergling.ktormovies.ui.screen.login.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.domain.usecase.user.GetUserDetailsUC

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