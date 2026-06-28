package com.enmanuelbergling.feature.series.watchlist

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveTvFromAccountWatchlistUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.feature.series.paging.usecase.GetPaginatedWatchlistSeriesUC
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistSeriesState())
    val uiState = _uiState.asStateFlow()

    val watchlist: Flow<PagingData<TvShow>> = getPaginatedWatchlistSeries()
        .cachedIn(viewModelScope)
        .combine(uiState.map { it.deletedSeriesIds }) { paging, deletedSeries ->
            paging.filter { it.id !in deletedSeries }
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
}

@Stable
internal data class WatchlistSeriesState(
    val uiState: SimplerUi = SimplerUi.Idle,
    val deletedSeriesIds: List<Int> = emptyList(),
)

internal sealed interface WatchlistSeriesSideEffect {
    data class NavigateToDetails(val seriesId: Int) : WatchlistSeriesSideEffect
    data class UndoRemoveSeries(val seriesId: Int) : WatchlistSeriesSideEffect
    data class RemoveSeriesError(val seriesId: Int) : WatchlistSeriesSideEffect
}

internal sealed interface WatchlistSeriesEvent {
    data class OnRemoveSeries(val seriesId: Int) : WatchlistSeriesEvent
    data class RemoveSeries(val seriesId: Int) : WatchlistSeriesEvent
    data class NavigateToDetails(val seriesId: Int) : WatchlistSeriesEvent
    data object UndoRemove : WatchlistSeriesEvent
    data class OnRemoveSeriesErrorDismissed(val seriesId: Int) : WatchlistSeriesEvent
}
