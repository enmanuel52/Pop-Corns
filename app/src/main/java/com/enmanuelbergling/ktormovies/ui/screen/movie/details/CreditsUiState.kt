package com.enmanuelbergling.ktormovies.ui.screen.movie.details

import com.enmanuelbergling.ktormovies.domain.model.movie.MovieCredits

sealed interface CreditsUiState {
    data object Loading : CreditsUiState
    data class Error(val message: String) : CreditsUiState

    data class Success(val data: MovieCredits?) : CreditsUiState
}