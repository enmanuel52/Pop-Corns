package com.enmanuelbergling.ktormovies.data.source.remote.dto.movie


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenreListDTO(
    @SerialName("genres")
    val genres: List<GenreDTO>,
)