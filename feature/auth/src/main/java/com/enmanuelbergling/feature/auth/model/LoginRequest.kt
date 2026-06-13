package com.enmanuelbergling.feature.auth.model

import com.enmanuelbergling.core.model.auth.CreateSessionPost

data class LoginRequest(
    val username: String = "",
    val password: String = "",
    val requestToken: String = "",
) {
    fun toCreateSessionPost() = CreateSessionPost(
        username = username,
        password = password,
        requestToken = requestToken
    )
}
