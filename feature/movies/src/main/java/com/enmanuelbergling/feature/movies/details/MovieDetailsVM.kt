package com.enmanuelbergling.feature.movies.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieAccountStatesUC
import com.enmanuelbergling.core.domain.usecase.user.favorite.AddMovieToFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.favorite.RemoveMovieFromFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddMovieToAccountWatchlistUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveMovieFromAccountWatchlistUC
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsChain
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MovieDetailsVM(
    private val movieDetailsChain: MovieDetailsChain,
    private val addMovieToAccountWatchlistUC: AddMovieToAccountWatchlistUC,
    private val removeMovieFromAccountWatchlistUC: RemoveMovieFromAccountWatchlistUC,
    private val addMovieToFavoritesUC: AddMovieToFavoritesUC,
    private val removeMovieFromFavoritesUC: RemoveMovieFromFavoritesUC,
    private val movieId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsState(movieId = movieId))
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = Channel<MovieDetailsEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        loadPage()
    }

    fun onAction(action: MovieDetailsAction) {
        when (action) {
            MovieDetailsAction.OnBack -> viewModelScope.launch {
                _uiEvents.send(MovieDetailsEvent.NavigateBack)
            }

            MovieDetailsAction.OnRetry -> loadPage()
            MovieDetailsAction.OnWatchlistClick -> addOrRemoveFromWatchlist()
            MovieDetailsAction.OnFavoriteClick -> addOrRemoveFromFavorites()
            is MovieDetailsAction.OnActorClick -> viewModelScope.launch {
                _uiEvents.send(MovieDetailsEvent.NavigateToActor(action.action))
            }
        }
    }

    private fun loadPage() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = SimplerUi.Loading) }
        runCatching {
            val request = _uiState.value.toChainRequest()

            val detailsChain = movieDetailsChain.detailsHandler.apply {
                nextChainHandler = movieDetailsChain.creditsHandler.apply {
                    nextChainHandler = movieDetailsChain.accountStatesHandler
                }
            }

            detailsChain.invoke(request)

            _uiState.update {
                it.copy(
                    details = request.details,
                    credits = request.credits,
                    accountStates = request.accountStates
                )
            }
        }.onFailure { throwable ->
            _uiState.update { it.copy(uiState = SimplerUi.Error(NetworkException.DefaultException.messageResource)) }
        }.onSuccess {
            _uiState.update { it.copy(uiState = SimplerUi.Idle) }
        }
    }

    private fun addOrRemoveFromWatchlist() = viewModelScope.launch {
        val isMovieInWatchlist = _uiState.value.accountStates?.watchlist ?: false

        _uiState.update { it.copy(isWatchlistLoading = true) }

        val result = if (isMovieInWatchlist) {
            removeMovieFromAccountWatchlistUC(movieId)
        } else {
            addMovieToAccountWatchlistUC(movieId)
        }

        when (result) {
            is ResultHandler.Error -> {
                _uiState.update {
                    it.copy(
                        isWatchlistLoading = false,
                        uiState = SimplerUi.Error(result.exception.messageResource)
                    )
                }
            }

            is ResultHandler.Success -> {
                _uiState.update {
                    it.copy(
                        isWatchlistLoading = false,
                        accountStates = it.accountStates?.copy(watchlist = !isMovieInWatchlist)
                    )
                }
            }
        }
    }

    private fun addOrRemoveFromFavorites() = viewModelScope.launch {
        val isMovieFavorite = _uiState.value.accountStates?.favorite ?: false

        _uiState.update { it.copy(isFavoriteLoading = true) }

        val result = if (isMovieFavorite) {
            removeMovieFromFavoritesUC(movieId)
        } else {
            addMovieToFavoritesUC(movieId)
        }

        when (result) {
            is ResultHandler.Error -> {
                _uiState.update {
                    it.copy(
                        isFavoriteLoading = false,
                        uiState = SimplerUi.Error(result.exception.messageResource)
                    )
                }
            }

            is ResultHandler.Success -> {
                _uiState.update {
                    it.copy(
                        isFavoriteLoading = false,
                        accountStates = it.accountStates?.copy(favorite = !isMovieFavorite)
                    )
                }
            }
        }
    }
}
