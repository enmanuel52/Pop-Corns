package com.enmanuelbergling.ktormovies.data.source.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductionCompanyDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("name")
    val name: String,
    @SerialName("origin_country")
    val originCountry: String
)