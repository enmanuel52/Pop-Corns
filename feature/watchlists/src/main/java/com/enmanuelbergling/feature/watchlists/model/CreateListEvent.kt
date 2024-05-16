package com.enmanuelbergling.feature.watchlists.model

sealed interface CreateListEvent {
    data class Name(val value: String) : CreateListEvent
    data class Description(val value: String) : CreateListEvent
    data object Submit : CreateListEvent

    data object ToggleVisibility: CreateListEvent
}