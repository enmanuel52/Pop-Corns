package com.enmanuelbergling.ktormovies.data.source.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MoviePageDTO(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<MovieDTO>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)