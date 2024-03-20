package com.enmanuelbergling.ktormovies.ui.screen.login.model

import com.enmanuelbergling.core.model.auth.CreateSessionPost
import kotlinx.coroutines.flow.MutableStateFlow

typealias LoginChainState = MutableStateFlow<LoginChain>

data class LoginChain(
    val username: String="",
    val password: String="",
    val requestToken: String= "",
){
    fun toCreateSessionPost() = CreateSessionPost(
        username = username,
        password = password,
        requestToken = requestToken
    )
}
