package com.enmanuelbergling.ktormovies.ui.screen.movie.details

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.BuildConfig
import com.enmanuelbergling.ktormovies.domain.model.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.domain.model.movie.BelongsToCollection
import com.enmanuelbergling.ktormovies.domain.model.user.AccountListsFilter
import com.enmanuelbergling.ktormovies.domain.model.user.WatchList
import com.enmanuelbergling.ktormovies.domain.usecase.auth.GetSavedSessionIdUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.watchlist.AddMovieToListUC
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.MovieDetailsChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.MovieDetailsUiData
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
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MovieDetailsVM(
    private val detailsChainHandler: MovieDetailsChainHandler,
    getPaginatedLists: GetFilteredPagingFlowUC<WatchList, AccountListsFilter>,
    getSessionId: GetSavedSessionIdUC,
    private val addMovieToListUC: AddMovieToListUC,
    movieId: Int,
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
                getPaginatedLists(
                    AccountListsFilter(
                        accountId = BuildConfig.ACCOUNT_ID,
                        sessionId = sessionId.value
                    )
                ).cachedIn(
                    viewModelScope
                )
            }

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

    fun addMovieToList(movieId: Int, listId: Int) = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        when (val result = addMovieToListUC(
            movieId = movieId,
            listId = listId,
            sessionId = sessionId.value
        )
        ) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.message.orEmpty()) }
            is ResultHandler.Success -> _uiState.update { SimplerUi.Success }
        }
    }
}