package com.enmanuelbergling.core.model.tv

import com.enmanuelbergling.core.model.movie.Genre


data class TvShowDetails(
    val id: Int,
    val name: String,
    val originalName: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val genres: List<Genre>,
    val firstAirDate: String,
    val lastAirDate: String,
    val numberOfSeasons: Int,
    val numberOfEpisodes: Int,
    val status: String,
    val tagline: String,
    val voteAverage: Double,
    val voteCount: Int,
    val seasons: List<Season>,
) {
    val firstAirYear: String
        get() = firstAirDate.let {
            if (it.length > 3) it.substring(0..3)
            else it
        }

    val formattedGenres: String
        get() = if (genres.isEmpty()) "Unavailable"
        else genres.joinToString("/") { it.name }
}
