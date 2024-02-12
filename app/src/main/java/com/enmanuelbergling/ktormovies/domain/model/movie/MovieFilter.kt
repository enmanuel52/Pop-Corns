package com.enmanuelbergling.ktormovies.domain.model.movie

data class MovieFilter(
    val genres: List<Genre> = listOf(),
    val sortBy: SortCriteria = SortCriteria.Popularity,
)

enum class SortCriteria(val string: String) {
    Popularity("popularity.desc"),
    VoteAverage("vote_average.desc"),
    VoteCount("vote_count.desc"),
    Revenue("revenue.desc")
}
