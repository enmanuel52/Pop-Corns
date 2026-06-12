package com.enmanuelbergling.core.network.dto.user.watch

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchlistBody(
    @SerialName("media_type")
    @Required
    val mediaType: String = "movie",
    @SerialName("media_id")
    val mediaId: Int,
    @SerialName("watchlist")
    val watchlist: Boolean,
)