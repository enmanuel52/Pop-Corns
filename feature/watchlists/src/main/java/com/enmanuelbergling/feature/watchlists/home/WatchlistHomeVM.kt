package com.enmanuelbergling.feature.watchlists.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enmanuelbergling.core.domain.usecase.user.favorite.AddMovieToFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveMovieFromAccountWatchlistUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.feature.watchlists.paging.GetPaginatedAccountWatchlistUC
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class WatchlistHomeVM(
    getPaginatedAccountWatchlist: GetPaginatedAccountWatchlistUC,
    private val removeMovieFromAccountWatchlistUC: RemoveMovieFromAccountWatchlistUC,
    private val addMovieToFavoritesUC: AddMovieToFavoritesUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistHomeState())
    val uiState = _uiState.asStateFlow()

    val watchlist: Flow<PagingData<Movie>> =
        getPaginatedAccountWatchlist().cachedIn(viewModelScope)

    private val _sideEffectChannel = Channel<WatchlistHomeSideEffect>()
    val sideEffectChannel get() = _sideEffectChannel.receiveAsFlow()

    fun onEvent(event: WatchlistHomeEvent) {
        when (event) {
            is WatchlistHomeEvent.OnDeleteMovie -> onDeleteMovie(event.movieId)
            is WatchlistHomeEvent.DeleteMovie -> removeFromWatchlist(event.movieId)
            WatchlistHomeEvent.DismissDialog -> onIdle()
            is WatchlistHomeEvent.UndoDelete -> undoDelete()
            is WatchlistHomeEvent.NavigateToDetails -> viewModelScope.launch {
                _sideEffectChannel.send(
                    WatchlistHomeSideEffect.NavigateToDetails(event.movieId)
                )
            }

            WatchlistHomeEvent.NavigateToLists -> viewModelScope.launch {
                _sideEffectChannel.send(WatchlistHomeSideEffect.NavigateToLists)
            }

            WatchlistHomeEvent.OpenDrawer -> viewModelScope.launch {
                _sideEffectChannel.send(WatchlistHomeSideEffect.OpenDrawer)
            }

            is WatchlistHomeEvent.OnDeleteMovieErrorDismissed -> {
                _uiState.update {
                    it.copy(deletedMovieIds = it.deletedMovieIds - event.movieId)
                }
            }

            is WatchlistHomeEvent.OnAddToFavorites -> onAddToFavorites(event.movieId)
            is WatchlistHomeEvent.AddToFavorites -> addToFavoritesAndRemoveFromWatchlist(event.movieId)
            WatchlistHomeEvent.UndoAddToFavorites -> undoAddToFavorites()
            is WatchlistHomeEvent.OnAddToFavoritesErrorDismissed -> {
                _uiState.update {
                    it.copy(favoritedMovieIds = it.favoritedMovieIds - event.movieId)
                }
            }
        }
    }

    private fun removeFromWatchlist(movieId: Int) = viewModelScope.launch {
        when (removeMovieFromAccountWatchlistUC(movieId)) {
            is ResultHandler.Error<*> -> _sideEffectChannel.send(
                WatchlistHomeSideEffect.DeleteMovieError(movieId)
            )
            is ResultHandler.Success<*> -> {}
        }
    }

    private fun undoDelete() = viewModelScope.launch {
        val movieId = _uiState.value.deletedMovieIds.lastOrNull() ?: return@launch
        _uiState.update {
            it.copy(deletedMovieIds = it.deletedMovieIds - movieId)
        }
    }

    private fun onDeleteMovie(movieId: Int) {
        _uiState.update {
            it.copy(deletedMovieIds = it.deletedMovieIds + movieId)
        }
        viewModelScope.launch {
            _sideEffectChannel.send(WatchlistHomeSideEffect.UndoDeleteMovie(movieId))
        }
    }

    private fun onAddToFavorites(movieId: Int) {
        _uiState.update {
            it.copy(favoritedMovieIds = it.favoritedMovieIds + movieId)
        }
        viewModelScope.launch {
            _sideEffectChannel.send(WatchlistHomeSideEffect.UndoAddToFavoritesMovie(movieId))
        }
    }

    private fun addToFavoritesAndRemoveFromWatchlist(movieId: Int) = viewModelScope.launch {
        val favResult = addMovieToFavoritesUC(movieId)
        val watchlistResult = removeMovieFromAccountWatchlistUC(movieId)
        if (favResult is ResultHandler.Error<*> || watchlistResult is ResultHandler.Error<*>) {
            _sideEffectChannel.send(WatchlistHomeSideEffect.AddToFavoritesError(movieId))
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
internal data class WatchlistHomeState(
    val uiState: SimplerUi = SimplerUi.Idle,
    val deletedMovieIds: List<Int> = emptyList(),
    val favoritedMovieIds: List<Int> = emptyList(),
)

internal val WatchlistHomeState.removedItems: List<Int>
    get() = deletedMovieIds + favoritedMovieIds

internal sealed interface WatchlistHomeSideEffect {
    data class NavigateToDetails(val movieId: Int) : WatchlistHomeSideEffect
    data class UndoDeleteMovie(val movieId: Int) : WatchlistHomeSideEffect
    data class UndoAddToFavoritesMovie(val movieId: Int) : WatchlistHomeSideEffect
    data class DeleteMovieError(val movieId: Int) : WatchlistHomeSideEffect
    data class AddToFavoritesError(val movieId: Int) : WatchlistHomeSideEffect
    data object NavigateToLists : WatchlistHomeSideEffect
    data object OpenDrawer : WatchlistHomeSideEffect
}

internal sealed interface WatchlistHomeEvent {
    data object DismissDialog : WatchlistHomeEvent
    data class OnDeleteMovie(val movieId: Int) : WatchlistHomeEvent
    data class DeleteMovie(val movieId: Int) : WatchlistHomeEvent
    data class OnAddToFavorites(val movieId: Int) : WatchlistHomeEvent
    data class AddToFavorites(val movieId: Int) : WatchlistHomeEvent
    data class NavigateToDetails(val movieId: Int) : WatchlistHomeEvent
    data object UndoDelete : WatchlistHomeEvent
    data object UndoAddToFavorites : WatchlistHomeEvent
    data object NavigateToLists : WatchlistHomeEvent
    data object OpenDrawer : WatchlistHomeEvent
    data class OnDeleteMovieErrorDismissed(val movieId: Int) : WatchlistHomeEvent
    data class OnAddToFavoritesErrorDismissed(val movieId: Int) : WatchlistHomeEvent
}
