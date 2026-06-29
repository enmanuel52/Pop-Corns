package com.enmanuelbergling.core.model.tv


data class SeasonDetails(
    val id: Int,
    val seasonNumber: Int,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val airDate: String?,
    val episodes: List<Episode>,
)
