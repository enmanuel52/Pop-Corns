package com.enmanuelbergling.core.network.dto.user.watch


import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.paging.model.PagingResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieListPageDTO(
    @SerialName("created_by")
    val createdBy: String,
    @SerialName("description")
    val description: String,
    @SerialName("favorite_count")
    val favoriteCount: Int,
    @SerialName("id")
    val id: String,
    @SerialName("items")
    val items: List<MovieDTO>,
    @SerialName("item_count")
    val itemCount: Int,
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("name")
    val name: String,
    @SerialName("poster_path")
    val posterPath: String?,
) : PagingResponse<MovieDTO> {
    override val totalPages: Int
        get() = itemCount
    override val results: List<MovieDTO>
        get() = items
}