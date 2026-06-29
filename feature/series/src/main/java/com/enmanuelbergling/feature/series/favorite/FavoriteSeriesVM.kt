package com.enmanuelbergling.feature.series.favorite

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.enmanuelbergling.core.domain.usecase.user.favorite.RemoveTvFromFavoritesUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.feature.series.paging.usecase.GetPaginatedFavoriteSeriesUC
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


internal class FavoriteSeriesVM(
    getPaginatedFavoriteSeries: GetPaginatedFavoriteSeriesUC,
    private val removeTvFromFavoritesUC: RemoveTvFromFavoritesUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteSeriesState())
    val uiState = _uiState.asStateFlow()

    val favorites: Flow<PagingData<TvShow>> = getPaginatedFavoriteSeries()
        .cachedIn(viewModelScope)
        .combine(uiState.map { it.deletedSeriesIds }) { paging, deletedSeries ->
            paging.filter { it.id !in deletedSeries }
        }

    private val _sideEffectChannel = Channel<FavoriteSeriesSideEffect>()
    val sideEffectChannel get() = _sideEffectChannel.receiveAsFlow()

    fun onEvent(event: FavoriteSeriesEvent) {
        when (event) {
            is FavoriteSeriesEvent.OnRemoveSeries -> onRemoveSeries(event.seriesId)
            is FavoriteSeriesEvent.RemoveSeries -> removeFromFavorites(event.seriesId)
            FavoriteSeriesEvent.UndoRemove -> undoRemove()
            is FavoriteSeriesEvent.NavigateToDetails -> viewModelScope.launch {
                _sideEffectChannel.send(FavoriteSeriesSideEffect.NavigateToDetails(event.seriesId))
            }

            is FavoriteSeriesEvent.OnRemoveSeriesErrorDismissed -> {
                _uiState.update {
                    it.copy(deletedSeriesIds = it.deletedSeriesIds - event.seriesId)
                }
            }
        }
    }

    private fun removeFromFavorites(seriesId: Int) = viewModelScope.launch {
        when (removeTvFromFavoritesUC(seriesId)) {
            is ResultHandler.Error<*> -> _sideEffectChannel.send(
                FavoriteSeriesSideEffect.RemoveSeriesError(seriesId)
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
            _sideEffectChannel.send(FavoriteSeriesSideEffect.UndoRemoveSeries(seriesId))
        }
    }
}

@Stable
internal data class FavoriteSeriesState(
    val uiState: SimplerUi = SimplerUi.Idle,
    val deletedSeriesIds: List<Int> = emptyList(),
)

internal sealed interface FavoriteSeriesSideEffect {
    data class NavigateToDetails(val seriesId: Int) : FavoriteSeriesSideEffect
    data class UndoRemoveSeries(val seriesId: Int) : FavoriteSeriesSideEffect
    data class RemoveSeriesError(val seriesId: Int) : FavoriteSeriesSideEffect
}

internal sealed interface FavoriteSeriesEvent {
    data class OnRemoveSeries(val seriesId: Int) : FavoriteSeriesEvent
    data class RemoveSeries(val seriesId: Int) : FavoriteSeriesEvent
    data class NavigateToDetails(val seriesId: Int) : FavoriteSeriesEvent
    data object UndoRemove : FavoriteSeriesEvent
    data class OnRemoveSeriesErrorDismissed(val seriesId: Int) : FavoriteSeriesEvent
}
