package com.enmanuelbergling.ktormovies.domain.model


data class ProductionCompany(
    val id: Int,
    val logoPath: String?,
    val name: String,
    val originCountry: String
)