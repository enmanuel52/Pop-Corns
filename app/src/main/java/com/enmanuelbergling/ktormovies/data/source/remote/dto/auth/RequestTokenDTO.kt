package com.enmanuelbergling.ktormovies.data.source.remote.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestTokenDTO(
    @SerialName("success") val success: Boolean,
    @SerialName("request_token") val token: String,
)
