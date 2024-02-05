package com.enmanuelbergling.ktormovies.ui.screen.login.model

import com.enmanuelbergling.ktormovies.domain.design.ChainHandler

class LoginChainHandler(
    private val firstHandler: CreateRequestTokenChainHandler,
) : ChainHandler<LoginChainState> {
    override val nextChainHandler: ChainHandler<LoginChainState>
        get() = firstHandler

    override suspend fun handle(request: LoginChainState) = Unit

}