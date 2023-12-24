package com.enmanuelbergling.ktormovies.data.source.remote.dto.movie


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenreDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)