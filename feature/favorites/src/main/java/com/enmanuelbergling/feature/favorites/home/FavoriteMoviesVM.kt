package com.enmanuelbergling.feature.favorites.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enmanuelbergling.core.domain.usecase.user.favorite.RemoveMovieFromFavoritesUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.feature.favorites.paging.GetPaginatedFavoriteMoviesUC
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


internal class FavoriteMoviesVM(
    getPaginatedFavoriteMovies: GetPaginatedFavoriteMoviesUC,
    private val removeMovieFromFavoritesUC: RemoveMovieFromFavoritesUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteMoviesState())
    val uiState = _uiState.asStateFlow()

    val favorites: Flow<PagingData<Movie>> =
        getPaginatedFavoriteMovies().cachedIn(viewModelScope)

    private val _sideEffectChannel = Channel<FavoriteMoviesSideEffect>()
    val sideEffectChannel get() = _sideEffectChannel.receiveAsFlow()

    fun onEvent(event: FavoriteMoviesEvent) {
        when (event) {
            is FavoriteMoviesEvent.OnRemoveMovie -> onRemoveMovie(event.movieId)
            is FavoriteMoviesEvent.RemoveMovie -> removeFromFavorites(event.movieId)
            FavoriteMoviesEvent.UndoRemove -> undoRemove()
            is FavoriteMoviesEvent.NavigateToDetails -> viewModelScope.launch {
                _sideEffectChannel.send(FavoriteMoviesSideEffect.NavigateToDetails(event.movieId))
            }

            is FavoriteMoviesEvent.OnRemoveMovieErrorDismissed -> {
                _uiState.update {
                    it.copy(deletedMovieIds = it.deletedMovieIds - event.movieId)
                }
            }
        }
    }

    private fun removeFromFavorites(movieId: Int) = viewModelScope.launch {
        when (removeMovieFromFavoritesUC(movieId)) {
            is ResultHandler.Error<*> -> _sideEffectChannel.send(
                FavoriteMoviesSideEffect.RemoveMovieError(movieId)
            )

            is ResultHandler.Success<*> -> {}
        }
    }

    private fun undoRemove() {
        val movieId = _uiState.value.deletedMovieIds.lastOrNull() ?: return
        _uiState.update { it.copy(deletedMovieIds = it.deletedMovieIds - movieId) }
    }

    private fun onRemoveMovie(movieId: Int) {
        _uiState.update { it.copy(deletedMovieIds = it.deletedMovieIds + movieId) }
        viewModelScope.launch {
            _sideEffectChannel.send(FavoriteMoviesSideEffect.UndoRemoveMovie(movieId))
        }
    }
}

@Stable
internal data class FavoriteMoviesState(
    val uiState: SimplerUi = SimplerUi.Idle,
    val deletedMovieIds: List<Int> = emptyList(),
)

internal sealed interface FavoriteMoviesSideEffect {
    data class NavigateToDetails(val movieId: Int) : FavoriteMoviesSideEffect
    data class UndoRemoveMovie(val movieId: Int) : FavoriteMoviesSideEffect
    data class RemoveMovieError(val movieId: Int) : FavoriteMoviesSideEffect
}

internal sealed interface FavoriteMoviesEvent {
    data class OnRemoveMovie(val movieId: Int) : FavoriteMoviesEvent
    data class RemoveMovie(val movieId: Int) : FavoriteMoviesEvent
    data class NavigateToDetails(val movieId: Int) : FavoriteMoviesEvent
    data object UndoRemove : FavoriteMoviesEvent
    data class OnRemoveMovieErrorDismissed(val movieId: Int) : FavoriteMoviesEvent
}
