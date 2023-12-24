package com.enmanuelbergling.ktormovies.data.source.remote.dto.movie


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SpokenLanguageDTO(
    @SerialName("english_name")
    val englishName: String,
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("name")
    val name: String
)