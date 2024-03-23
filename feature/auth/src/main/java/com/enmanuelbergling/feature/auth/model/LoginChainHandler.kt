package com.enmanuelbergling.feature.auth.model

import com.enmanuelbergling.core.domain.design.ChainHandler

class LoginChainHandler(
    private val firstHandler: CreateRequestTokenChainHandler,
) : ChainHandler<LoginChainState> {
    override val nextChainHandler: ChainHandler<LoginChainState>
        get() = firstHandler

    override suspend fun handle(request: LoginChainState) = Unit

}