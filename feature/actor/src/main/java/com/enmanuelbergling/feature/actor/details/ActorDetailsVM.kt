package com.enmanuelbergling.feature.actor.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsChain
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActorDetailsVM(
    private val actorDetailsChain: ActorDetailsChain,
    private val actorId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActorDetailsState(actorId = actorId))
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = Channel<ActorDetailsEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        loadPage()
    }

    fun onAction(action: ActorDetailsAction) {
        when (action) {
            ActorDetailsAction.OnBack -> viewModelScope.launch {
                _uiEvents.send(ActorDetailsEvent.NavigateBack)
            }

            ActorDetailsAction.OnRetry -> loadPage()
            is ActorDetailsAction.OnMovieClick -> viewModelScope.launch {
                _uiEvents.send(ActorDetailsEvent.NavigateToMovie(action.movieId))
            }
        }
    }

    private fun loadPage() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = SimplerUi.Loading) }
        runCatching {
            val request = _uiState.value.toRequest()

            val chain = actorDetailsChain.detailsHandler.apply {
                nextChainHandler = actorDetailsChain.knownMoviesHandler
            }

            chain.invoke(request)

            _uiState.update {
                it.copy(
                    details = request.details,
                    knownMovies = request.knownMovies
                )
            }
        }.onFailure {
            _uiState.update { it.copy(uiState = SimplerUi.Error(NetworkException.DefaultException.messageResource)) }
        }.onSuccess {
            _uiState.update { it.copy(uiState = SimplerUi.Idle) }
        }
    }
}
