package com.enmanuelbergling.feature.movies.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.auth.GetSavedSessionIdUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieAccountStatesUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddMovieToAccountWatchlistUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveMovieFromAccountWatchlistUC
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.movies.details.model.AccountStatesChainHandler
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsChainHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

internal class MovieDetailsVM(
    private val detailsChainHandler: MovieDetailsChainHandler,
    getSessionId: GetSavedSessionIdUC,
    private val addMovieToAccountWatchlistUC: AddMovieToAccountWatchlistUC,
    private val removeMovieFromAccountWatchlistUC: RemoveMovieFromAccountWatchlistUC,
    private val movieId: Int,
) : ViewModel(), KoinComponent {

    private val _uiState = MutableStateFlow(MovieDetailsState(movieId = movieId))
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = Channel<MovieDetailsEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            getSessionId().collect { session ->
                _uiState.update { it.copy(sessionId = session) }
                loadPage()
            }
        }
    }

    fun onAction(action: MovieDetailsAction) {
        when (action) {
            MovieDetailsAction.OnBack -> viewModelScope.launch {
                _uiEvents.send(MovieDetailsEvent.NavigateBack)
            }

            MovieDetailsAction.OnRetry -> loadPage()
            MovieDetailsAction.OnWatchlistClick -> addOrRemoveFromWatchlist()
            is MovieDetailsAction.OnActorClick -> viewModelScope.launch {
                _uiEvents.send(MovieDetailsEvent.NavigateToActor(action.action))
            }
        }
    }

    private fun loadPage() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = SimplerUi.Loading) }
        runCatching {
            val session = _uiState.value.sessionId
            val accountStatesHandler: AccountStatesChainHandler =
                get { parametersOf(session) }

            val request = _uiState.value.toChainRequest()
            detailsChainHandler.invoke(request)
            accountStatesHandler.handle(request)

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
        val session = _uiState.value.sessionId
        val isMovieInWatchlist = _uiState.value.accountStates?.watchlist ?: false

        _uiState.update { it.copy(isWatchlistLoading = true) }

        val result = if (isMovieInWatchlist) {
            removeMovieFromAccountWatchlistUC(movieId, session)
        } else {
            addMovieToAccountWatchlistUC(movieId, session)
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
}
