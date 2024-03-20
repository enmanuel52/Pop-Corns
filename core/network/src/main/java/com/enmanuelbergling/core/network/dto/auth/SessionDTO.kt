package com.enmanuelbergling.core.network.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionDTO(
    @SerialName("success") val success: Boolean,
    @SerialName("session_id") val sessionId: String,
)
