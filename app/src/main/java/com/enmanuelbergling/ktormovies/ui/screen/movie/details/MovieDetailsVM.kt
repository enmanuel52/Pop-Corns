package com.enmanuelbergling.ktormovies.ui.screen.movie.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.ktormovies.domain.model.movie.MovieDetails
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.usecase.GetMovieCreditsUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetMovieDetailsUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailsVM(
    private val getMovieDetailsUC: GetMovieDetailsUC,
    private val getMovieCreditsUC: GetMovieCreditsUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUi>(MovieDetailsUi.Idle)
    val uiState = _uiState.asStateFlow()

    private val _creditsState = MutableStateFlow<CreditsUiState>(CreditsUiState.Loading)
    val creditsState get() = _creditsState.asStateFlow()

    private val _detailsState = MutableStateFlow<MovieDetails?>(null)
    val detailsState get() = _detailsState.asStateFlow()

    fun getDetails(id: Int) = viewModelScope.launch {
        _uiState.update { MovieDetailsUi.Loading }
        when (val result = getMovieDetailsUC(id)) {
            is ResultHandler.Error -> _uiState.update { MovieDetailsUi.Error(result.exception.message.orEmpty()) }
            is ResultHandler.Success -> {
                _detailsState.update { result.data }
                _uiState.update { MovieDetailsUi.Success }
                //once the main details are loaded
                getMovieCredits(id)
            }
        }
    }

    fun hideErrorDialog() {
        _uiState.update { MovieDetailsUi.Idle }
    }

    fun getMovieCredits(id: Int) = viewModelScope.launch {
        _creditsState.update { CreditsUiState.Loading }
        when (val result = getMovieCreditsUC(id)) {
            is ResultHandler.Error -> _creditsState.update {
                CreditsUiState.Error(
                    result.exception.message ?: "An error has been ocurred loading cast and crew"
                )
            }

            is ResultHandler.Success -> _creditsState.update {
                val credits = result.data?.copy(
                    cast = result.data.cast.distinctBy { it.name },
                    crew = result.data.crew.distinctBy { it.name },
                )
                CreditsUiState.Success(credits)
            }
        }
    }
}