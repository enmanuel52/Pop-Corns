package com.enmanuelbergling.ktormovies.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.ktormovies.domain.model.MovieDetails
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.usecase.GetMovieDetailsUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsVM(private val getMovieDetailsUC: GetMovieDetailsUC) : ViewModel() {

    private val _movieDetailsState = MutableStateFlow<MovieDetails?>(null)
    val movieDetailsState get() = _movieDetailsState.asStateFlow()

    private val _uiState = MutableStateFlow<DetailsUi>(DetailsUi.Idle)
    val uiState get() = _uiState.asStateFlow()

    fun getDetails(id: Int) = viewModelScope.launch {
        _uiState.update { DetailsUi.Loading }
        when (val result = getMovieDetailsUC(id)) {
            is ResultHandler.Error -> _uiState.update { DetailsUi.Error(result.exception) }
            is ResultHandler.Success -> {
                _uiState.update { DetailsUi.Success }
                _movieDetailsState.update { result.data }
            }
        }
    }

    fun dismissDialog(){
        _uiState.update { DetailsUi.Idle }
    }
}

sealed interface DetailsUi {
    data object Loading : DetailsUi

    data class Error(val exception: Exception) : DetailsUi

    data object Idle : DetailsUi

    data object Success : DetailsUi
}