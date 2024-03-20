package com.enmanuelbergling.core.model.auth


data class CreateSessionPost(
    val username: String,
    val password: String,
    val requestToken: String,
)
