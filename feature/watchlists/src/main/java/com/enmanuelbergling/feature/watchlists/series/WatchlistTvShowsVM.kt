package com.enmanuelbergling.feature.watchlists.tvShows

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.enmanuelbergling.core.domain.usecase.user.favorite.AddTvToFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveTvFromAccountWatchlistUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.feature.watchlists.paging.GetPaginatedWatchlistTvShowsUC
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


internal class WatchlistTvShowsVM(
    getPaginatedWatchlistTvShows: GetPaginatedWatchlistTvShowsUC,
    private val removeTvFromAccountWatchlistUC: RemoveTvFromAccountWatchlistUC,
    private val addTvToFavoritesUC: AddTvToFavoritesUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistTvShowsState())
    val uiState = _uiState.asStateFlow()

    val watchlist: Flow<PagingData<TvShow>> = getPaginatedWatchlistTvShows()
        .cachedIn(viewModelScope)
        .combine(uiState.map { it.removedItems }) { paging, removed ->
            paging.filter { it.id !in removed }
        }

    private val _sideEffectChannel = Channel<WatchlistTvShowsSideEffect>()
    val sideEffectChannel get() = _sideEffectChannel.receiveAsFlow()

    fun onEvent(event: WatchlistTvShowsEvent) {
        when (event) {
            is WatchlistTvShowsEvent.OnRemoveTvShows -> onRemoveTvShows(event.tvShowId)
            is WatchlistTvShowsEvent.RemoveTvShows -> removeFromWatchlist(event.tvShowId)
            WatchlistTvShowsEvent.UndoRemove -> undoRemove()
            is WatchlistTvShowsEvent.NavigateToDetails -> viewModelScope.launch {
                _sideEffectChannel.send(WatchlistTvShowsSideEffect.NavigateToDetails(event.tvShowId))
            }

            is WatchlistTvShowsEvent.OnRemoveTvShowsErrorDismissed -> {
                _uiState.update {
                    it.copy(deletedTvShowsIds = it.deletedTvShowsIds - event.tvShowId)
                }
            }

            is WatchlistTvShowsEvent.OnAddToFavorites -> onAddToFavorites(event.tvShowId)
            is WatchlistTvShowsEvent.AddToFavorites -> addToFavoritesAndRemoveFromWatchlist(event.tvShowId)
            WatchlistTvShowsEvent.UndoAddToFavorites -> undoAddToFavorites()
            is WatchlistTvShowsEvent.OnAddToFavoritesErrorDismissed -> {
                _uiState.update {
                    it.copy(favoritedTvShowsIds = it.favoritedTvShowsIds - event.tvShowId)
                }
            }
        }
    }

    private fun removeFromWatchlist(tvShowId: Int) = viewModelScope.launch {
        when (removeTvFromAccountWatchlistUC(tvShowId)) {
            is ResultHandler.Error<*> -> _sideEffectChannel.send(
                WatchlistTvShowsSideEffect.RemoveTvShowsError(tvShowId)
            )

            is ResultHandler.Success<*> -> {}
        }
    }

    private fun undoRemove() {
        val tvShowId = _uiState.value.deletedTvShowsIds.lastOrNull() ?: return
        _uiState.update { it.copy(deletedTvShowsIds = it.deletedTvShowsIds - tvShowId) }
    }

    private fun onRemoveTvShows(tvShowId: Int) {
        _uiState.update { it.copy(deletedTvShowsIds = it.deletedTvShowsIds + tvShowId) }
        viewModelScope.launch {
            _sideEffectChannel.send(WatchlistTvShowsSideEffect.UndoRemoveTvShows(tvShowId))
        }
    }

    private fun onAddToFavorites(tvShowId: Int) {
        _uiState.update { it.copy(favoritedTvShowsIds = it.favoritedTvShowsIds + tvShowId) }
        viewModelScope.launch {
            _sideEffectChannel.send(WatchlistTvShowsSideEffect.UndoAddToFavoritesTvShows(tvShowId))
        }
    }

    private fun addToFavoritesAndRemoveFromWatchlist(tvShowId: Int) = viewModelScope.launch {
        val favResult = addTvToFavoritesUC(tvShowId)
        val watchlistResult = removeTvFromAccountWatchlistUC(tvShowId)
        if (favResult is ResultHandler.Error<*> || watchlistResult is ResultHandler.Error<*>) {
            _sideEffectChannel.send(WatchlistTvShowsSideEffect.AddToFavoritesError(tvShowId))
        }
    }

    private fun undoAddToFavorites() {
        val tvShowId = _uiState.value.favoritedTvShowsIds.lastOrNull() ?: return
        _uiState.update { it.copy(favoritedTvShowsIds = it.favoritedTvShowsIds - tvShowId) }
    }
}

@Stable
internal data class WatchlistTvShowsState(
    val uiState: SimplerUi = SimplerUi.Idle,
    val deletedTvShowsIds: List<Int> = emptyList(),
    val favoritedTvShowsIds: List<Int> = emptyList(),
)

internal val WatchlistTvShowsState.removedItems: List<Int>
    get() = deletedTvShowsIds + favoritedTvShowsIds

internal sealed interface WatchlistTvShowsSideEffect {
    data class NavigateToDetails(val tvShowId: Int) : WatchlistTvShowsSideEffect
    data class UndoRemoveTvShows(val tvShowId: Int) : WatchlistTvShowsSideEffect
    data class RemoveTvShowsError(val tvShowId: Int) : WatchlistTvShowsSideEffect
    data class UndoAddToFavoritesTvShows(val tvShowId: Int) : WatchlistTvShowsSideEffect
    data class AddToFavoritesError(val tvShowId: Int) : WatchlistTvShowsSideEffect
}

internal sealed interface WatchlistTvShowsEvent {
    data class OnRemoveTvShows(val tvShowId: Int) : WatchlistTvShowsEvent
    data class RemoveTvShows(val tvShowId: Int) : WatchlistTvShowsEvent
    data class NavigateToDetails(val tvShowId: Int) : WatchlistTvShowsEvent
    data object UndoRemove : WatchlistTvShowsEvent
    data class OnRemoveTvShowsErrorDismissed(val tvShowId: Int) : WatchlistTvShowsEvent
    data class OnAddToFavorites(val tvShowId: Int) : WatchlistTvShowsEvent
    data class AddToFavorites(val tvShowId: Int) : WatchlistTvShowsEvent
    data object UndoAddToFavorites : WatchlistTvShowsEvent
    data class OnAddToFavoritesErrorDismissed(val tvShowId: Int) : WatchlistTvShowsEvent
}
