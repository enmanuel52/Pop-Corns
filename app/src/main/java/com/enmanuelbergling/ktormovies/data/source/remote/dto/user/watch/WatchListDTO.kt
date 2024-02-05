package com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class WatchListDTO(
    @SerialName("description")
    val description: String,
    @SerialName("favorite_count")
    val favoriteCount: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("item_count")
    val itemCount: Int,
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("list_type")
    val listType: String,
    @SerialName("name")
    val name: String,
    @SerialName("poster_path")
    val posterPath: String?
)