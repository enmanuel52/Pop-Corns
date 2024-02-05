package com.enmanuelbergling.ktormovies.domain.model.user


 data class MovieList(
    val description: String,
    val favoriteCount: Int,
    val id: Int,
    val itemCount: Int,
    val iso6391: String,
    val listType: String,
    val name: String,
    val posterPath: String?,
)