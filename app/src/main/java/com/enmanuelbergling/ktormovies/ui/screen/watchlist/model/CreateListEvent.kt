package com.enmanuelbergling.ktormovies.ui.screen.watchlist.model

sealed interface CreateListEvent {
    data class Name(val value: String) : CreateListEvent
    data class Description(val value: String) : CreateListEvent
    data object Submit : CreateListEvent

    data object OpenForm: CreateListEvent
}