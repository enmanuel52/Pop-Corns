package com.enmanuelbergling.feature.movies.search

import com.enmanuelbergling.core.domain.datasource.preferences.StringQuery

sealed interface SuggestionEvent {
    data class Add(val query: String): SuggestionEvent
    data class Delete(val query: String): SuggestionEvent
    data object Clear: SuggestionEvent
}