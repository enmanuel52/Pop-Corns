package com.enmanuelbergling.feature.movies.home.model

sealed interface SuggestionEvent {
    data class Add(val query: String): SuggestionEvent
    data class Delete(val query: String): SuggestionEvent
    data object Clear: SuggestionEvent
}