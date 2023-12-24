package com.enmanuelbergling.ktormovies.domain.model.movie


data class ProductionCompany(
    val id: Int,
    val logoPath: String?,
    val name: String,
    val originCountry: String
)