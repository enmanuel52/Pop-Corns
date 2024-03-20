package com.enmanuelbergling.ktormovies.ui.screen.movie.home

import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.ui.components.messageResource
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.model.MoviesChainStart
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.model.MoviesUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MoviesVM(
    private val homeMoviesHandler: MoviesChainStart,
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
        }.onFailure { _ ->
            _uiState.update { SimplerUi.Error(NetworkException.ReadTimeOutException.messageResource) }
        }.onSuccess {
            _uiState.update { SimplerUi.Idle }
        }
    }

}