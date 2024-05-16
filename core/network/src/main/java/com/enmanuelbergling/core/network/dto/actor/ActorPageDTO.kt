package com.enmanuelbergling.core.network.dto.actor


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ActorPageDTO(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
     val results: List<ActorDTO>,
    @SerialName("total_pages")
     val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
)