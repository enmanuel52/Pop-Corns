package com.enmanuelbergling.ktormovies.ui.screen.movie.details

sealed interface MovieDetailsUi {
    data object Loading : MovieDetailsUi
    data object Idle : MovieDetailsUi
    data object Success : MovieDetailsUi
    data class Error(val message: String) : MovieDetailsUi
}