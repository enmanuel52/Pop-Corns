package com.enmanuelbergling.core.model.actor

data class KnownMovie(
    val id: Int,
    val posterPath: String?,
    val title: String,
    val voteAverage: Double,
)
