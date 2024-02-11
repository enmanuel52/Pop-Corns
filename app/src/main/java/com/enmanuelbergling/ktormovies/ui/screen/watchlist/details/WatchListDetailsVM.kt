package com.enmanuelbergling.ktormovies.ui.screen.watchlist.details

import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.domain.model.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.domain.usecase.auth.GetSavedSessionIdUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.watchlist.DeleteMovieFromListUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class WatchListDetailsVM(
    getWatchListMovies: GetFilteredPagingFlowUC<Movie, Int>,
    private val getSessionId: GetSavedSessionIdUC,
    private val deleteMovieFromListUC: DeleteMovieFromListUC,
    private val listId: Int,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    val movies = getWatchListMovies(listId).cachedIn(viewModelScope)

    fun deleteMovieFromList(
        movieId: Int,
    ) = viewModelScope.launch {
        val sessionId = getSessionId().firstOrNull().orEmpty()
        _uiState.update { SimplerUi.Loading }
        when (val result = deleteMovieFromListUC(
            movieId = movieId,
            listId = listId,
            sessionId = sessionId
        )) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.message.orEmpty()) }
            is ResultHandler.Success -> {
                _uiState.update { SimplerUi.Success }
            }
        }
    }

    fun onIdle() {
        _uiState.update { SimplerUi.Idle }
    }
}