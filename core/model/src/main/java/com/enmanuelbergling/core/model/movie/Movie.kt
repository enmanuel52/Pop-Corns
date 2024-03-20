package com.enmanuelbergling.core.model.movie


data class Movie(
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
) {
    val releaseYear: String
        get() = releaseDate.let {
            if (it.length > 3) it.substring(0..3)
            else it
        }
}