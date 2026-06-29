package com.enmanuelbergling.feature.series.home.model

import com.enmanuelbergling.core.domain.datasource.preferences.StringQuery
import com.enmanuelbergling.core.model.tv.TvShow

data class SeriesUiData(
    val popular: List<TvShow> = emptyList(),
    val topRated: List<TvShow> = emptyList(),
    val onTheAir: List<TvShow> = emptyList(),
    val airingToday: List<TvShow> = emptyList(),
    val searchQuery: String = "",
    val searchSuggestions: List<StringQuery> = emptyList(),
    val searchSuggestionsDeleted: List<StringQuery> = emptyList(),
)
