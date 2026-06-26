package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.domain.datasource.preferences.StringQuery
import com.enmanuelbergling.core.model.movie.Movie

data class MoviesUiData(
    val upcoming: List<Movie> = emptyList(),
    val topRated: List<Movie> = emptyList(),
    val nowPlaying: List<Movie> = emptyList(),
    val popular: List<Movie> = emptyList(),
    val searchQuery: String = "",
    val searchSuggestions: List<StringQuery> = emptyList(),
    val searchSuggestionsDeleted: List<StringQuery> = emptyList(),
    val startOnSearch: Boolean = false,
)