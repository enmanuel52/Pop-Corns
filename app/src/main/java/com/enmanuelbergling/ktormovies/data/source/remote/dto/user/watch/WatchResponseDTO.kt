package com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class WatchResponseDTO(
    @SerialName("status_message")
    val statusMessage: String
)