package com.enmanuelbergling.core.network.dto.movie


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenreDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)