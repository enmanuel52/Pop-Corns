package com.enmanuelbergling.ktormovies.domain.model.actor


internal data class Actor(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String,
    val knownFor: List<ActorKnownFor>,
)