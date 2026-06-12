package com.enmanuelbergling.core.network.dto.movie

import kotlinx.serialization.Serializable

@Serializable
internal data class MovieAccountStatesDTO(
    val id: Int,
    val favorite: Boolean,
    val watchlist: Boolean,
)