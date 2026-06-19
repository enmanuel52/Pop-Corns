package com.enmanuelbergling.feature.watchlists.details

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.core.domain.usecase.user.favorite.AddMovieToFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.DeleteMovieFromListUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.feature.watchlists.paging.GetWatchListMoviesUC
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class WatchListDetailsVM(
    getWatchListMovies: GetWatchListMoviesUC,
    private val deleteMovieFromListUC: DeleteMovieFromListUC,
    private val addMovieToFavoritesUC: AddMovieToFavoritesUC,
    private val listId: Int,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WatchlistDetailsState())
    val uiState = _uiState.asStateFlow()

    val movies = getWatchListMovies(listId).cachedIn(viewModelScope)

    private val _sideEffectChannel = Channel<WatchlistDetailsSideEffect>()
    val sideEffectChannel get() = _sideEffectChannel.receiveAsFlow()

    fun onEvent(event: WatchlistDetailsEvent) {
        when (event) {
            is WatchlistDetailsEvent.OnDeleteMovie -> onDeleteMovie(event.movieId)
            is WatchlistDetailsEvent.DeleteMovie -> deleteMovieFromList(event.movieId)
            WatchlistDetailsEvent.DismissDialog -> onIdle()
            is WatchlistDetailsEvent.UndoDelete -> undoDelete()
            WatchlistDetailsEvent.NavigateBack -> viewModelScope.launch {
                _sideEffectChannel.send(
                    WatchlistDetailsSideEffect.NavigateBack
                )
            }

            is WatchlistDetailsEvent.NavigateToDetails -> viewModelScope.launch {
                _sideEffectChannel.send(
                    WatchlistDetailsSideEffect.NavigateToDetails(event.movieId)
                )
            }

            WatchlistDetailsEvent.OnAddShortcut -> viewModelScope.launch {
                _sideEffectChannel.send(
                    WatchlistDetailsSideEffect.OnAddShortcut
                )
            }

            WatchlistDetailsEvent.OnDeleteShortCut -> viewModelScope.launch {
                _sideEffectChannel.send(
                    WatchlistDetailsSideEffect.OnDeleteShortCut
                )
            }

            is WatchlistDetailsEvent.OnDeleteMovieErrorDismissed -> {
                _uiState.update {
                    it.copy(
                        deletedMovieIds = it.deletedMovieIds - event.movieId
                    )
                }
            }

            is WatchlistDetailsEvent.OnAddToFavorites -> onAddToFavorites(event.movieId)
            is WatchlistDetailsEvent.AddToFavorites -> addToFavoritesAndRemoveFromList(event.movieId)
            WatchlistDetailsEvent.UndoAddToFavorites -> undoAddToFavorites()
            is WatchlistDetailsEvent.OnAddToFavoritesErrorDismissed -> {
                _uiState.update {
                    it.copy(favoritedMovieIds = it.favoritedMovieIds - event.movieId)
                }
            }
        }
    }

    private fun deleteMovieFromList(
        movieId: Int,
    ) = viewModelScope.launch {
        when (deleteMovieFromListUC(
            movieId = movieId,
            listId = listId
        )) {
            is ResultHandler.Error -> _sideEffectChannel.send(
                WatchlistDetailsSideEffect.DeleteMovieError(movieId)
            )

            is ResultHandler.Success -> {}
        }
    }

    private fun undoDelete() = viewModelScope.launch {
        val movieId = _uiState.value.deletedMovieIds.lastOrNull() ?: return@launch
        _uiState.update {
            it.copy(
                deletedMovieIds = it.deletedMovieIds - movieId
            )
        }
    }

    private fun onDeleteMovie(movieId: Int) {
        _uiState.update {
            it.copy(
                deletedMovieIds = it.deletedMovieIds + movieId
            )
        }
        viewModelScope.launch {
            _sideEffectChannel.send(WatchlistDetailsSideEffect.UndoDeleteMovie(movieId))
        }
    }

    private fun onAddToFavorites(movieId: Int) {
        _uiState.update {
            it.copy(favoritedMovieIds = it.favoritedMovieIds + movieId)
        }
        viewModelScope.launch {
            _sideEffectChannel.send(WatchlistDetailsSideEffect.UndoAddToFavoritesMovie(movieId))
        }
    }

    private fun addToFavoritesAndRemoveFromList(movieId: Int) = viewModelScope.launch {
        val favResult = addMovieToFavoritesUC(movieId)
        val listResult = deleteMovieFromListUC(movieId = movieId, listId = listId)
        if (favResult is ResultHandler.Error<*> || listResult is ResultHandler.Error<*>) {
            _sideEffectChannel.send(WatchlistDetailsSideEffect.AddToFavoritesError(movieId))
        }
    }

    private fun undoAddToFavorites() = viewModelScope.launch {
        val movieId = _uiState.value.favoritedMovieIds.lastOrNull() ?: return@launch
        _uiState.update {
            it.copy(favoritedMovieIds = it.favoritedMovieIds - movieId)
        }
    }

    private fun onIdle() {
        _uiState.update { it.copy(uiState = SimplerUi.Idle) }
    }
}

@Stable
internal data class WatchlistDetailsState(
    val uiState: SimplerUi = SimplerUi.Idle,
    val deletedMovieIds: List<Int> = emptyList(),
    val favoritedMovieIds: List<Int> = emptyList(),
)

internal sealed interface WatchlistDetailsSideEffect {
    data class NavigateToDetails(val movieId: Int) : WatchlistDetailsSideEffect
    data object NavigateBack : WatchlistDetailsSideEffect
    data object OnAddShortcut : WatchlistDetailsSideEffect
    data object OnDeleteShortCut : WatchlistDetailsSideEffect
    data class UndoDeleteMovie(val movieId: Int) : WatchlistDetailsSideEffect
    data class DeleteMovieError(val movieId: Int) : WatchlistDetailsSideEffect
    data class UndoAddToFavoritesMovie(val movieId: Int) : WatchlistDetailsSideEffect
    data class AddToFavoritesError(val movieId: Int) : WatchlistDetailsSideEffect
}

internal sealed interface WatchlistDetailsEvent {
    data object DismissDialog : WatchlistDetailsEvent
    data class OnDeleteMovie(val movieId: Int) : WatchlistDetailsEvent
    data class DeleteMovie(val movieId: Int) : WatchlistDetailsEvent
    data class NavigateToDetails(val movieId: Int) : WatchlistDetailsEvent
    data object NavigateBack : WatchlistDetailsEvent
    data object UndoDelete : WatchlistDetailsEvent
    data object OnAddShortcut : WatchlistDetailsEvent
    data object OnDeleteShortCut : WatchlistDetailsEvent
    data class OnDeleteMovieErrorDismissed(val movieId: Int) : WatchlistDetailsEvent
    data class OnAddToFavorites(val movieId: Int) : WatchlistDetailsEvent
    data class AddToFavorites(val movieId: Int) : WatchlistDetailsEvent
    data object UndoAddToFavorites : WatchlistDetailsEvent
    data class OnAddToFavoritesErrorDismissed(val movieId: Int) : WatchlistDetailsEvent
}
