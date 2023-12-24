package com.enmanuelbergling.ktormovies.data.source.remote.dto.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class KnownMovieDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("title")
    val title: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    )
