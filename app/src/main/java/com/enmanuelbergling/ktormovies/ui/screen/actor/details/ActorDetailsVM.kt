package com.enmanuelbergling.ktormovies.ui.screen.actor.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.ktormovies.domain.model.actor.ActorDetails
import com.enmanuelbergling.ktormovies.domain.model.actor.KnownMovie
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.domain.usecase.GetActorDetailsUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetMoviesByActorUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActorDetailsVM(
    actorId: Int,
    private val getActorDetailsUC: GetActorDetailsUC,
    private val getMoviesByActorUC: GetMoviesByActorUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    private val _detailsState = MutableStateFlow<ActorDetails?>(null)
    val detailsState get() = _detailsState.asStateFlow()

    private val _knownMoviesState = MutableStateFlow(listOf<KnownMovie>())
    val knownMoviesState get() = _knownMoviesState.asStateFlow()

    init {
        getDetails(actorId)
    }

    private fun getDetails(actorId: Int) = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        when (val result = getActorDetailsUC(actorId)) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.message.orEmpty()) }
            is ResultHandler.Success -> {
                _detailsState.update { result.data }
                _uiState.update { SimplerUi.Success }
                getMoviesByActor(actorId)
            }
        }
    }

    private fun getMoviesByActor(actorId: Int) = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        when (val result = getMoviesByActorUC(actorId)) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.message.orEmpty()) }
            is ResultHandler.Success -> {
                _knownMoviesState.update { result.data.orEmpty() }
                _uiState.update { SimplerUi.Success }
            }
        }
    }

    fun onDismissDialog() {
        _uiState.update { SimplerUi.Idle }
    }
}