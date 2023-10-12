package com.enmanuelbergling.ktormovies.domain.model


data class MovieDetails(
    val adult: Boolean,
    val backdropPath: String,
    val belongsToCollection: BelongsToCollection?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdbId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val productionCompanies: List<ProductionCompany>,
    val productionCountries: List<ProductionCountry>,
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
) {
    val formattedGenres: String
        get() = buildString {
            if (genres.isEmpty()) append("Unavailable")
            else {
                genres.forEachIndexed { index, genre ->
                    if (index == 0) append(" - " + genre.name)
                    else append("/${genre.name}")
                }
            }
        }

    val duration: String
        get() = buildString {
            val hours = runtime / 60
            append("$hours h")
            val minutes = runtime % 60
            append("m$minutes m")
        }
}