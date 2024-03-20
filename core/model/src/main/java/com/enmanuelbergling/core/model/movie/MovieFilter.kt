package com.enmanuelbergling.core.model.movie

data class MovieFilter(
    val genres: List<Genre> = listOf(),
    val sortBy: SortCriteria = SortCriteria.Popularity,
)

enum class SortCriteria {
    Popularity,
    VoteAverage,
    VoteCount,
    Revenue
}
