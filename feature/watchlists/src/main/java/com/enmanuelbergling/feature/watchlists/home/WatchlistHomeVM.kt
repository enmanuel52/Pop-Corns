package com.enmanuelbergling.feature.watchlists.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enmanuelbergling.core.domain.usecase.auth.GetSavedSessionIdUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveMovieFromAccountWatchlistUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.watchlists.paging.GetPaginatedAccountWatchlistUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class WatchlistHomeVM(
    getPaginatedAccountWatchlist: GetPaginatedAccountWatchlistUC,
    getSessionId: GetSavedSessionIdUC,
    private val removeMovieFromAccountWatchlistUC: RemoveMovieFromAccountWatchlistUC,
) : ViewModel() {

    private val sessionId = getSessionId().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val watchlist: Flow<PagingData<Movie>> =
        sessionId.filter { it.isNotBlank() }
            .flatMapLatest {
                getPaginatedAccountWatchlist(it).cachedIn(viewModelScope)
            }

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    fun removeFromWatchlist(movieId: Int) = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        when (val result = removeMovieFromAccountWatchlistUC(movieId, sessionId.value)) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.messageResource) }
            is ResultHandler.Success -> _uiState.update { SimplerUi.Success }
        }
    }

    fun onIdle() {
        _uiState.update { SimplerUi.Idle }
    }
}