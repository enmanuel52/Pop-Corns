package com.enmanuelbergling.ktormovies.domain.model.actor

data class KnownMovie(
    val id: Int,
    val posterPath: String?,
    val title: String,
    val voteAverage: Double,
)
