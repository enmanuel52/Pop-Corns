package com.enmanuelbergling.core.domain.usecase.movie.search

import com.enmanuelbergling.core.domain.datasource.preferences.SearchSuggestionDS

class GetSearchSuggestionsUC(
    private val repo: SearchSuggestionDS
) {
     operator fun invoke()=repo.getSuggestions()
}