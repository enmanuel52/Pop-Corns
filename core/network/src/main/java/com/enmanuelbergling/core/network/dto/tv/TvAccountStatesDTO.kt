package com.enmanuelbergling.core.network.dto.tv

import kotlinx.serialization.Serializable

@Serializable
internal data class TvAccountStatesDTO(
    val id: Int,
    val favorite: Boolean,
    val watchlist: Boolean,
)
