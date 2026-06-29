package com.enmanuelbergling.core.testing.datasource.preference

import com.enmanuelbergling.core.domain.datasource.preferences.SearchSuggestionDS
import com.enmanuelbergling.core.domain.datasource.preferences.StringQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeSearchSuggestionDS : SearchSuggestionDS {
    private val _suggestions = MutableStateFlow(emptyList<StringQuery>())

    override suspend fun addSuggestion(query: StringQuery) {
        _suggestions.update { it.plus(query).distinct() }
    }

    override suspend fun deleteSuggestion(query: StringQuery) {
        _suggestions.update { it.minus(query) }
    }

    override suspend fun clearSuggestions() {
        _suggestions.update { emptyList() }
    }

    override fun getSuggestions(): Flow<List<StringQuery>> = _suggestions.asStateFlow()
}
