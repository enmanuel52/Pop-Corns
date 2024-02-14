package com.enmanuelbergling.ktormovies.domain.model.movie

import androidx.annotation.StringRes
import com.enmanuelbergling.ktormovies.R

data class MovieFilter(
    val genres: List<Genre> = listOf(),
    val sortBy: SortCriteria = SortCriteria.Popularity,
)

enum class SortCriteria(val string: String,@StringRes val label: Int) {
    Popularity("popularity.desc", R.string.popularity),
    VoteAverage("vote_average.desc", R.string.vote_average),
    VoteCount("vote_count.desc", R.string.vote_count),
    Revenue("revenue.desc", R.string.revenue)
}
