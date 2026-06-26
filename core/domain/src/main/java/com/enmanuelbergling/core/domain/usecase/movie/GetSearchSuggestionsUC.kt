package com.enmanuelbergling.core.domain.usecase.movie

import com.enmanuelbergling.core.domain.datasource.preferences.SearchSuggestionDS

class GetSearchSuggestionsUC(
    private val localDs: SearchSuggestionDS,
) {
    operator fun invoke() = localDs.getSuggestions()

    suspend fun delete(query: String)=localDs.deleteSuggestion(query)

    suspend fun clear()=localDs.clearSuggestions()

    suspend fun add(query: String) = localDs.addSuggestion(query)
}