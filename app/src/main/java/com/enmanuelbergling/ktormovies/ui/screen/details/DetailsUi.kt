package com.enmanuelbergling.ktormovies.ui.screen.details

sealed interface DetailsUi {
    data object Loading : DetailsUi
    data object Idle : DetailsUi
    data object Success : DetailsUi
    data class Error(val message: String) : DetailsUi
}