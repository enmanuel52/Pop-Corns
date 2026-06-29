package com.enmanuelbergling.feature.tvshows.tvshowdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.usecase.user.favorite.AddTvToFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.favorite.RemoveTvFromFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddTvToAccountWatchlistUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveTvFromAccountWatchlistUC
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.tvshows.tvshowdetails.model.TvShowDetailsChain
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class TvShowDetailsVM(
    private val tvShowDetailsChain: TvShowDetailsChain,
    private val addTvToAccountWatchlistUC: AddTvToAccountWatchlistUC,
    private val removeTvFromAccountWatchlistUC: RemoveTvFromAccountWatchlistUC,
    private val addTvToFavoritesUC: AddTvToFavoritesUC,
    private val removeTvFromFavoritesUC: RemoveTvFromFavoritesUC,
    private val tvShowId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TvShowDetailsState(tvShowId = tvShowId))
    val uiState = _uiState
        .onStart { loadPage() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TvShowDetailsState(tvShowId = tvShowId)
        )

    private val _uiEvents = Channel<TvShowDetailsEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onAction(action: TvShowDetailsAction) {
        when (action) {
            TvShowDetailsAction.OnBack -> viewModelScope.launch {
                _uiEvents.send(TvShowDetailsEvent.NavigateBack)
            }

            TvShowDetailsAction.OnRetry -> loadPage()
            TvShowDetailsAction.OnWatchlistClick -> addOrRemoveFromWatchlist()
            TvShowDetailsAction.OnFavoriteClick -> addOrRemoveFromFavorites()
            TvShowDetailsAction.OnSeeAllSeasons -> viewModelScope.launch {
                _uiEvents.send(TvShowDetailsEvent.NavigateToSeasons)
            }

            is TvShowDetailsAction.OnSeasonClick -> viewModelScope.launch {
                _uiEvents.send(TvShowDetailsEvent.NavigateToEpisodes(action.seasonNumber))
            }

            is TvShowDetailsAction.OnActorClick -> viewModelScope.launch {
                _uiEvents.send(TvShowDetailsEvent.NavigateToActor(action.action))
            }
        }
    }

    private fun loadPage() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = SimplerUi.Loading) }
        val request = _uiState.value.toChainRequest()

        runCatching {
            val detailsChain = tvShowDetailsChain.detailsHandler.apply {
                nextChainHandler = tvShowDetailsChain.creditsHandler.apply {
                    nextChainHandler = tvShowDetailsChain.accountStatesHandler
                }
            }

            try {
                detailsChain.invoke(request)
            } finally {
                _uiState.update {
                    it.copy(
                        details = request.details,
                        credits = request.credits,
                        accountStates = request.accountStates
                    )
                }
            }
        }.onFailure { throwable ->
            val networkException =
                (throwable as? CannotHandleException)?.throwable as? NetworkException
            val messageRes = networkException?.messageResource
                ?: NetworkException.DefaultException().messageResource

            _uiState.update { it.copy(uiState = SimplerUi.Error(messageRes)) }
        }.onSuccess {
            _uiState.update { it.copy(uiState = SimplerUi.Idle) }
        }
    }

    private fun addOrRemoveFromWatchlist() = viewModelScope.launch {
        val inWatchlist = _uiState.value.accountStates?.watchlist ?: false

        _uiState.update { it.copy(isWatchlistLoading = true) }

        val result = if (inWatchlist) {
            removeTvFromAccountWatchlistUC(tvShowId)
        } else {
            addTvToAccountWatchlistUC(tvShowId)
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
            removeTvFromFavoritesUC(tvShowId)
        } else {
            addTvToFavoritesUC(tvShowId)
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
