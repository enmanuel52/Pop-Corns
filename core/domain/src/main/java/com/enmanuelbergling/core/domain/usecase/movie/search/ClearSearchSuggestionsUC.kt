package com.enmanuelbergling.core.domain.usecase.movie.search

import com.enmanuelbergling.core.domain.datasource.preferences.SearchSuggestionDS

class ClearSearchSuggestionsUC(
    private val repo: SearchSuggestionDS
) {
    suspend operator fun invoke()=repo.clearSuggestions()
}