package com.enmanuelbergling.core.network.dto.actor

import kotlinx.serialization.Serializable

@Serializable
internal data class CastWrapperDTO(
    val cast: List<KnownMovieDTO>,
)
