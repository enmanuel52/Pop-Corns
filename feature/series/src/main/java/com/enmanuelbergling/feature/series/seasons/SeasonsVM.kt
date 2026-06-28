package com.enmanuelbergling.feature.series.seasons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.tv.GetTvAccountStatesUC
import com.enmanuelbergling.core.domain.usecase.tv.GetTvDetailsUC
import com.enmanuelbergling.core.domain.usecase.user.favorite.AddTvToFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.favorite.RemoveTvFromFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddTvToAccountWatchlistUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveTvFromAccountWatchlistUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.components.messageResource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SeasonsVM(
    private val getTvDetailsUC: GetTvDetailsUC,
    private val getTvAccountStatesUC: GetTvAccountStatesUC,
    private val addTvToAccountWatchlistUC: AddTvToAccountWatchlistUC,
    private val removeTvFromAccountWatchlistUC: RemoveTvFromAccountWatchlistUC,
    private val addTvToFavoritesUC: AddTvToFavoritesUC,
    private val removeTvFromFavoritesUC: RemoveTvFromFavoritesUC,
    private val seriesId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeasonsState(seriesId = seriesId))
    val uiState = _uiState
        .onStart { loadPage() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SeasonsState(seriesId = seriesId)
        )

    private val _uiEvents = Channel<SeasonsEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onAction(action: SeasonsAction) {
        when (action) {
            SeasonsAction.OnBack -> viewModelScope.launch {
                _uiEvents.send(SeasonsEvent.NavigateBack)
            }

            SeasonsAction.OnRetry -> loadPage()
            SeasonsAction.OnWatchlistClick -> addOrRemoveFromWatchlist()
            SeasonsAction.OnFavoriteClick -> addOrRemoveFromFavorites()
            is SeasonsAction.OnSeasonClick -> viewModelScope.launch {
                _uiEvents.send(SeasonsEvent.NavigateToEpisodes(action.seasonNumber))
            }
        }
    }

    private fun loadPage() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = SimplerUi.Loading) }

        when (val detailsResult = getTvDetailsUC(seriesId)) {
            is ResultHandler.Error -> {
                _uiState.update {
                    it.copy(uiState = SimplerUi.Error(detailsResult.exception.messageResource))
                }
                return@launch
            }

            is ResultHandler.Success -> _uiState.update { it.copy(details = detailsResult.data) }
        }

        // Account states require an authenticated user; a failure here is non-fatal:
        // the seasons still show, just without the watchlist/favorite actions.
        when (val statesResult = getTvAccountStatesUC(seriesId)) {
            is ResultHandler.Error -> _uiState.update {
                it.copy(accountStates = null, uiState = SimplerUi.Idle)
            }

            is ResultHandler.Success -> _uiState.update {
                it.copy(accountStates = statesResult.data, uiState = SimplerUi.Idle)
            }
        }
    }

    private fun addOrRemoveFromWatchlist() = viewModelScope.launch {
        val inWatchlist = _uiState.value.accountStates?.watchlist ?: false

        _uiState.update { it.copy(isWatchlistLoading = true) }

        val result = if (inWatchlist) {
            removeTvFromAccountWatchlistUC(seriesId)
        } else {
            addTvToAccountWatchlistUC(seriesId)
        }

        when (result) {
            is ResultHandler.Error -> _uiState.update {
                it.copy(
                    isWatchlistLoading = false,
                    uiState = SimplerUi.Error(result.exception.messageResource)
                )
            }

            is ResultHandler.Success -> _uiState.update {
                it.copy(
                    isWatchlistLoading = false,
                    accountStates = it.accountStates?.copy(watchlist = !inWatchlist)
                )
            }
        }
    }

    private fun addOrRemoveFromFavorites() = viewModelScope.launch {
        val isFavorite = _uiState.value.accountStates?.favorite ?: false

        _uiState.update { it.copy(isFavoriteLoading = true) }

        val result = if (isFavorite) {
            removeTvFromFavoritesUC(seriesId)
        } else {
            addTvToFavoritesUC(seriesId)
        }

        when (result) {
            is ResultHandler.Error -> _uiState.update {
                it.copy(
                    isFavoriteLoading = false,
                    uiState = SimplerUi.Error(result.exception.messageResource)
                )
            }

            is ResultHandler.Success -> _uiState.update {
                it.copy(
                    isFavoriteLoading = false,
                    accountStates = it.accountStates?.copy(favorite = !isFavorite)
                )
            }
        }
    }
}
