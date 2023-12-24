package com.enmanuelbergling.ktormovies.data.source.remote.dto.movie


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductionCountryDTO(
    @SerialName("iso_3166_1")
    val iso31661: String,
    @SerialName("name")
    val name: String
)