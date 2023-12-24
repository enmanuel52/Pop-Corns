package com.enmanuelbergling.ktormovies.data.source.remote.dto.actor

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import kotlinx.serialization.Serializable

@Serializable
internal data class CastWrapperDTO(
    val cast: List<KnownMovieDTO>,
)
