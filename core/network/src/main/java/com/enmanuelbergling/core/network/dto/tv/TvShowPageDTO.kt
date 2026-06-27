package com.enmanuelbergling.core.network.dto.tv


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TvShowPageDTO(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<TvShowDTO>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
)
