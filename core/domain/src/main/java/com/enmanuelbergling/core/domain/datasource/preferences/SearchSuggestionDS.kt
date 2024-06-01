package com.enmanuelbergling.core.domain.datasource.preferences

import kotlinx.coroutines.flow.Flow

typealias StringQuery = String

interface SearchSuggestionDS {
    suspend fun addSuggestion(query: StringQuery)
    suspend fun deleteSuggestion(query: StringQuery)
    suspend fun clearSuggestions()

    fun getSuggestions(): Flow<List<StringQuery>>
}