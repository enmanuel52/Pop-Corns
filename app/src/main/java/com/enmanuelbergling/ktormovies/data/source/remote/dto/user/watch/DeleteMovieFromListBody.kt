package com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DeleteMovieFromListBody(
    @SerialName("media_id") val movieId: Int,
)
