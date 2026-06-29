package com.enmanuelbergling.feature.tvshows.favorite

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
import com.enmanuelbergling.feature.tvshows.paging.usecase.GetPaginatedFavoriteTvShowsUC
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


internal class FavoriteTvShowsVM(
    getPaginatedFavoriteTvShows: GetPaginatedFavoriteTvShowsUC,
    private val removeTvFromFavoritesUC: RemoveTvFromFavoritesUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteTvShowsState())
    val uiState = _uiState.asStateFlow()

    val favorites: Flow<PagingData<TvShow>> = getPaginatedFavoriteTvShows()
        .cachedIn(viewModelScope)
        .combine(uiState.map { it.deletedTvShowsIds }) { paging, deletedTvShows ->
            paging.filter { it.id !in deletedTvShows }
        }

    private val _sideEffectChannel = Channel<FavoriteTvShowsSideEffect>()
    val sideEffectChannel get() = _sideEffectChannel.receiveAsFlow()

    fun onEvent(event: FavoriteTvShowsEvent) {
        when (event) {
            is FavoriteTvShowsEvent.OnRemoveTvShows -> onRemoveTvShows(event.tvShowId)
            is FavoriteTvShowsEvent.RemoveTvShows -> removeFromFavorites(event.tvShowId)
            FavoriteTvShowsEvent.UndoRemove -> undoRemove()
            is FavoriteTvShowsEvent.NavigateToDetails -> viewModelScope.launch {
                _sideEffectChannel.send(FavoriteTvShowsSideEffect.NavigateToDetails(event.tvShowId))
            }

            is FavoriteTvShowsEvent.OnRemoveTvShowsErrorDismissed -> {
                _uiState.update {
                    it.copy(deletedTvShowsIds = it.deletedTvShowsIds - event.tvShowId)
                }
            }
        }
    }

    private fun removeFromFavorites(tvShowId: Int) = viewModelScope.launch {
        when (removeTvFromFavoritesUC(tvShowId)) {
            is ResultHandler.Error<*> -> _sideEffectChannel.send(
                FavoriteTvShowsSideEffect.RemoveTvShowsError(tvShowId)
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
            _sideEffectChannel.send(FavoriteTvShowsSideEffect.UndoRemoveTvShows(tvShowId))
        }
    }
}

@Stable
internal data class FavoriteTvShowsState(
    val uiState: SimplerUi = SimplerUi.Idle,
    val deletedTvShowsIds: List<Int> = emptyList(),
)

internal sealed interface FavoriteTvShowsSideEffect {
    data class NavigateToDetails(val tvShowId: Int) : FavoriteTvShowsSideEffect
    data class UndoRemoveTvShows(val tvShowId: Int) : FavoriteTvShowsSideEffect
    data class RemoveTvShowsError(val tvShowId: Int) : FavoriteTvShowsSideEffect
}

internal sealed interface FavoriteTvShowsEvent {
    data class OnRemoveTvShows(val tvShowId: Int) : FavoriteTvShowsEvent
    data class RemoveTvShows(val tvShowId: Int) : FavoriteTvShowsEvent
    data class NavigateToDetails(val tvShowId: Int) : FavoriteTvShowsEvent
    data object UndoRemove : FavoriteTvShowsEvent
    data class OnRemoveTvShowsErrorDismissed(val tvShowId: Int) : FavoriteTvShowsEvent
}
