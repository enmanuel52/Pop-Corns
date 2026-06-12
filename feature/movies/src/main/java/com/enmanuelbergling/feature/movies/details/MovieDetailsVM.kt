package com.enmanuelbergling.feature.movies.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.auth.GetSavedSessionIdUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieAccountStatesUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddMovieToAccountWatchlistUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddMovieToListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveMovieFromAccountWatchlistUC
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsChainHandler
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MovieDetailsVM(
    private val detailsChainHandler: MovieDetailsChainHandler,
    private val getMovieAccountStatesUC: GetMovieAccountStatesUC,
    getSessionId: GetSavedSessionIdUC,
    private val addMovieToListUC: AddMovieToListUC,
    private val addMovieToAccountWatchlistUC: AddMovieToAccountWatchlistUC,
    private val removeMovieFromAccountWatchlistUC: RemoveMovieFromAccountWatchlistUC,
    private val movieId: Int,
) : ViewModel() {

    private val sessionId = getSessionId().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiDataState = MutableStateFlow(MovieDetailsUiData(movieId = movieId))
    val uiDataState get() = _uiDataState.asStateFlow()

    init {
        loadPage()
        getAccountStates()
    }

    fun loadPage() = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        runCatching {
            detailsChainHandler.invoke(_uiDataState)
        }.onFailure { throwable ->
            _uiState.update { SimplerUi.Error(NetworkException.DefaultException.messageResource) }
        }.onSuccess {
            _uiState.update { SimplerUi.Idle }
        }
    }

    private fun getAccountStates() = viewModelScope.launch {
        sessionId.collect { session ->
            if (session.isNotBlank()) {
                when (val result = getMovieAccountStatesUC(movieId, session)) {
                    is ResultHandler.Error -> {}
                    is ResultHandler.Success -> {
                        _uiDataState.update { it.copy(accountStates = result.data) }
                    }
                }
            }
        }
    }

    fun addMovieToList(movieId: Int, list: WatchList) = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        when (val result = addMovieToListUC(
            movieId = movieId,
            listId = list.id,
            sessionId = sessionId.value
        )
        ) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.messageResource) }
            is ResultHandler.Success -> {
                _uiState.update { SimplerUi.Success }
            }
        }
    }

    fun toggleWatchlist(inWatchlist: Boolean) = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        val result = if (inWatchlist) {
            removeMovieFromAccountWatchlistUC(movieId, sessionId.value)
        } else {
            addMovieToAccountWatchlistUC(movieId, sessionId.value)
        }

        when (result) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.messageResource) }
            is ResultHandler.Success -> {
                _uiState.update { SimplerUi.Success }
                getAccountStates()
            }
        }
    }
}