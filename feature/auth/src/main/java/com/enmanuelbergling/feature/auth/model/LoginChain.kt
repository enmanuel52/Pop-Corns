package com.enmanuelbergling.feature.auth.model

data class LoginChain(
    val createRequestTokenHandler: CreateRequestTokenChainHandler,
    val createSessionFromLoginHandler: CreateSessionFromLoginChainHandler,
    val createSessionIdHandler: CreateSessionIdChainHandler,
    val getUserDetailsHandler: GetUserDetailsChainHandler,
)
