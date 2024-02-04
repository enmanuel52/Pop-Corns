package com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch


import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.model.PagingResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AccountListsPageDTO(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    override val results: List<MovieListDTO>,
    @SerialName("total_pages")
    override val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
) : PagingResponse<MovieListDTO>