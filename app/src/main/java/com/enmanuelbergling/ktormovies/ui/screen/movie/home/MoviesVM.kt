package com.enmanuelbergling.ktormovies.ui.screen.movie.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.model.HomeMoviesChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.model.MoviesUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesVM(
    private val homeMoviesHandler: HomeMoviesChainHandler,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiDataState = MutableStateFlow(MoviesUiData())
    val uiDataState get() = _uiDataState.asStateFlow()

    init {
        loadUi()
    }

    fun loadUi() = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        runCatching {
            homeMoviesHandler.invoke(_uiDataState)
        }.onFailure { throwable ->
            _uiState.update { SimplerUi.Error(throwable.message.orEmpty()) }
        }.onSuccess {
            _uiState.update { SimplerUi.Idle }
        }
    }

}