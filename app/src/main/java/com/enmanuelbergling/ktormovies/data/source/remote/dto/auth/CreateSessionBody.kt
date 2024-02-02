package com.enmanuelbergling.ktormovies.data.source.remote.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateSessionBody(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
    @SerialName("request_token") val requestToken: String,
)
