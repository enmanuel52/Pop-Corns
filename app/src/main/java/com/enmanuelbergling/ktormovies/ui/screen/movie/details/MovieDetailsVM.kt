package com.enmanuelbergling.ktormovies.ui.screen.movie.details

import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.MovieDetailsChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.MovieDetailsUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MovieDetailsVM(
    private val detailsChainHandler: MovieDetailsChainHandler,
    movieId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiDataState = MutableStateFlow(MovieDetailsUiData(movieId = movieId))
    val uiDataState get() = _uiDataState.asStateFlow()

    init {
        loadPage()
    }

    fun loadPage() = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        runCatching {
            detailsChainHandler.invoke(_uiDataState)
        }.onFailure { throwable ->
            _uiState.update { SimplerUi.Error(throwable.message.orEmpty()) }
        }.onSuccess {
            _uiState.update { SimplerUi.Idle }
        }
    }
}