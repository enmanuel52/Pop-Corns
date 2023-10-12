package com.enmanuelbergling.ktormovies.ui.screen.details

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

    private val _uiState = MutableStateFlow<DetailsUi>(DetailsUi.Idle)
    val uiState = _uiState.asStateFlow()

    private val _detailsState = MutableStateFlow<MovieDetails?>(null)
    val detailsState get() = _detailsState.asStateFlow()

    fun getDetails(id: Int) = viewModelScope.launch {
        when (val result = getMovieDetailsUC(id)) {
            is ResultHandler.Error -> _uiState.update { DetailsUi.Error(result.exception.message.orEmpty()) }
            is ResultHandler.Success -> {
                _detailsState.update { result.data }
                _uiState.update { DetailsUi.Success }
            }
        }
    }

    fun hideErrorDialog(){
        _uiState.update { DetailsUi.Idle }
    }
}