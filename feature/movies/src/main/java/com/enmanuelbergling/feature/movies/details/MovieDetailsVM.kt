package com.enmanuelbergling.feature.movies.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enmanuelbergling.core.common.util.TAG
import com.enmanuelbergling.core.domain.usecase.auth.GetSavedSessionIdUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddMovieToListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.CheckItemStatusUC
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.user.AccountListsFilter
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsChainHandler
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsUiData
import com.enmanuelbergling.feature.movies.paging.watchlist.GetUserWatchListsUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MovieDetailsVM(
    private val detailsChainHandler: MovieDetailsChainHandler,
    getWatchLists: GetUserWatchListsUC,
    getSessionId: GetSavedSessionIdUC,
    private val addMovieToListUC: AddMovieToListUC,
    private val checkItemStatusUC: CheckItemStatusUC,
    private val movieId: Int,
) : ViewModel() {

    val sessionId = getSessionId().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val watchlists: Flow<PagingData<WatchList>> =
        sessionId.filter { it.isNotBlank() }
            .flatMapLatest {
                getWatchLists(
                    AccountListsFilter(
                        sessionId = sessionId.value
                    )
                ).cachedIn(
                    viewModelScope
                )
            }.catch {
                Log.d(TAG, "watchlist paginated flow: ${it.message}")
            }

    private val _withinListsState = MutableStateFlow(listOf<WatchList>())
    val withinListsState get() = _withinListsState.asStateFlow()

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
            _uiState.update { SimplerUi.Error(NetworkException.DefaultException.messageResource) }
        }.onSuccess {
            _uiState.update { SimplerUi.Idle }
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
                _withinListsState.update {
                    (it + list).distinct()
                }
            }
        }
    }

    fun checkMovieOnLists(lists: List<WatchList>) = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        lists.forEach { list ->
            when (val result = checkItemStatusUC(listId = list.id, movieId = movieId)) {
                is ResultHandler.Error -> {
                    _uiState.update { SimplerUi.Error(result.exception.messageResource) }
                    return@forEach
                }

                is ResultHandler.Success -> {
                    val withinList = result.data == true
                    if (withinList) {
                        _withinListsState.update {
                            (it + list).distinct()
                        }
                    }
                }
            }
        }
        _uiState.update { SimplerUi.Success }
    }
}