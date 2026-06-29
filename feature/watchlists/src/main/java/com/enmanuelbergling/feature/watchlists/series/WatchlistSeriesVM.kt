package com.enmanuelbergling.feature.watchlists.series

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
import com.enmanuelbergling.feature.watchlists.paging.GetPaginatedWatchlistSeriesUC
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


internal class WatchlistSeriesVM(
    getPaginatedWatchlistSeries: GetPaginatedWatchlistSeriesUC,
    private val removeTvFromAccountWatchlistUC: RemoveTvFromAccountWatchlistUC,
    private val addTvToFavoritesUC: AddTvToFavoritesUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistSeriesState())
    val uiState = _uiState.asStateFlow()

    val watchlist: Flow<PagingData<TvShow>> = getPaginatedWatchlistSeries()
        .cachedIn(viewModelScope)
        .combine(uiState.map { it.removedItems }) { paging, removed ->
            paging.filter { it.id !in removed }
        }

    private val _sideEffectChannel = Channel<WatchlistSeriesSideEffect>()
    val sideEffectChannel get() = _sideEffectChannel.receiveAsFlow()

    fun onEvent(event: WatchlistSeriesEvent) {
        when (event) {
            is WatchlistSeriesEvent.OnRemoveSeries -> onRemoveSeries(event.seriesId)
            is WatchlistSeriesEvent.RemoveSeries -> removeFromWatchlist(event.seriesId)
            WatchlistSeriesEvent.UndoRemove -> undoRemove()
            is WatchlistSeriesEvent.NavigateToDetails -> viewModelScope.launch {
                _sideEffectChannel.send(WatchlistSeriesSideEffect.NavigateToDetails(event.seriesId))
            }

            is WatchlistSeriesEvent.OnRemoveSeriesErrorDismissed -> {
                _uiState.update {
                    it.copy(deletedSeriesIds = it.deletedSeriesIds - event.seriesId)
                }
            }

            is WatchlistSeriesEvent.OnAddToFavorites -> onAddToFavorites(event.seriesId)
            is WatchlistSeriesEvent.AddToFavorites -> addToFavoritesAndRemoveFromWatchlist(event.seriesId)
            WatchlistSeriesEvent.UndoAddToFavorites -> undoAddToFavorites()
            is WatchlistSeriesEvent.OnAddToFavoritesErrorDismissed -> {
                _uiState.update {
                    it.copy(favoritedSeriesIds = it.favoritedSeriesIds - event.seriesId)
                }
            }
        }
    }

    private fun removeFromWatchlist(seriesId: Int) = viewModelScope.launch {
        when (removeTvFromAccountWatchlistUC(seriesId)) {
            is ResultHandler.Error<*> -> _sideEffectChannel.send(
                WatchlistSeriesSideEffect.RemoveSeriesError(seriesId)
            )

            is ResultHandler.Success<*> -> {}
        }
    }

    private fun undoRemove() {
        val seriesId = _uiState.value.deletedSeriesIds.lastOrNull() ?: return
        _uiState.update { it.copy(deletedSeriesIds = it.deletedSeriesIds - seriesId) }
    }

    private fun onRemoveSeries(seriesId: Int) {
        _uiState.update { it.copy(deletedSeriesIds = it.deletedSeriesIds + seriesId) }
        viewModelScope.launch {
            _sideEffectChannel.send(WatchlistSeriesSideEffect.UndoRemoveSeries(seriesId))
        }
    }

    private fun onAddToFavorites(seriesId: Int) {
        _uiState.update { it.copy(favoritedSeriesIds = it.favoritedSeriesIds + seriesId) }
        viewModelScope.launch {
            _sideEffectChannel.send(WatchlistSeriesSideEffect.UndoAddToFavoritesSeries(seriesId))
        }
    }

    private fun addToFavoritesAndRemoveFromWatchlist(seriesId: Int) = viewModelScope.launch {
        val favResult = addTvToFavoritesUC(seriesId)
        val watchlistResult = removeTvFromAccountWatchlistUC(seriesId)
        if (favResult is ResultHandler.Error<*> || watchlistResult is ResultHandler.Error<*>) {
            _sideEffectChannel.send(WatchlistSeriesSideEffect.AddToFavoritesError(seriesId))
        }
    }

    private fun undoAddToFavorites() {
        val seriesId = _uiState.value.favoritedSeriesIds.lastOrNull() ?: return
        _uiState.update { it.copy(favoritedSeriesIds = it.favoritedSeriesIds - seriesId) }
    }
}

@Stable
internal data class WatchlistSeriesState(
    val uiState: SimplerUi = SimplerUi.Idle,
    val deletedSeriesIds: List<Int> = emptyList(),
    val favoritedSeriesIds: List<Int> = emptyList(),
)

internal val WatchlistSeriesState.removedItems: List<Int>
    get() = deletedSeriesIds + favoritedSeriesIds

internal sealed interface WatchlistSeriesSideEffect {
    data class NavigateToDetails(val seriesId: Int) : WatchlistSeriesSideEffect
    data class UndoRemoveSeries(val seriesId: Int) : WatchlistSeriesSideEffect
    data class RemoveSeriesError(val seriesId: Int) : WatchlistSeriesSideEffect
    data class UndoAddToFavoritesSeries(val seriesId: Int) : WatchlistSeriesSideEffect
    data class AddToFavoritesError(val seriesId: Int) : WatchlistSeriesSideEffect
}

internal sealed interface WatchlistSeriesEvent {
    data class OnRemoveSeries(val seriesId: Int) : WatchlistSeriesEvent
    data class RemoveSeries(val seriesId: Int) : WatchlistSeriesEvent
    data class NavigateToDetails(val seriesId: Int) : WatchlistSeriesEvent
    data object UndoRemove : WatchlistSeriesEvent
    data class OnRemoveSeriesErrorDismissed(val seriesId: Int) : WatchlistSeriesEvent
    data class OnAddToFavorites(val seriesId: Int) : WatchlistSeriesEvent
    data class AddToFavorites(val seriesId: Int) : WatchlistSeriesEvent
    data object UndoAddToFavorites : WatchlistSeriesEvent
    data class OnAddToFavoritesErrorDismissed(val seriesId: Int) : WatchlistSeriesEvent
}
