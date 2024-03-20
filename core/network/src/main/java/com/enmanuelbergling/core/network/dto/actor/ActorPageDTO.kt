package com.enmanuelbergling.core.network.dto.actor


import com.enmanuelbergling.core.network.paging.model.PagingResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ActorPageDTO(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    override val results: List<ActorDTO>,
    @SerialName("total_pages")
    override val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
) : PagingResponse<ActorDTO>