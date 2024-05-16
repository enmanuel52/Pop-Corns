package com.enmanuelbergling.core.network.dto.user.watch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AccountListsPageDTO(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<WatchListDTO>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
)