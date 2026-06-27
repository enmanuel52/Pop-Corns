package com.enmanuelbergling.core.model.tv


data class TvShow(
    val id: Int,
    val name: String,
    val originalName: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val originalLanguage: String,
    val popularity: Double,
    val firstAirDate: String,
    val voteAverage: Double,
    val voteCount: Int,
) {
    val firstAirYear: String
        get() = firstAirDate.let {
            if (it.length > 3) it.substring(0..3)
            else it
        }
}
