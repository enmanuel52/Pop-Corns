package com.enmanuelbergling.core.domain.usecase.movie.search

import com.enmanuelbergling.core.domain.datasource.preferences.SearchSuggestionDS

class DeleteSearchSuggestionUC(
    private val localDs: SearchSuggestionDS,
) {
    suspend operator fun invoke(query: String)=localDs.deleteSuggestion(query)
}