package com.enmanuelbergling.feature.series.episodedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.tv.GetEpisodeDetailsUC
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

internal class EpisodeDetailsVM(
    private val getEpisodeDetailsUC: GetEpisodeDetailsUC,
    private val seriesId: Int,
    private val seasonNumber: Int,
    private val episodeNumber: Int,
) : ViewModel() {

    private val initialState = EpisodeDetailsState(
        seriesId = seriesId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
    )

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState
        .onStart { loadPage() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialState
        )

    private val _uiEvents = Channel<EpisodeDetailsUiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onAction(action: EpisodeDetailsAction) {
        when (action) {
            EpisodeDetailsAction.OnBack -> viewModelScope.launch {
                _uiEvents.send(EpisodeDetailsUiEvent.NavigateBack)
            }

            EpisodeDetailsAction.OnRetry -> loadPage()
            is EpisodeDetailsAction.OnActorClick -> viewModelScope.launch {
                _uiEvents.send(EpisodeDetailsUiEvent.NavigateToActor(action.action))
            }
        }
    }

    private fun loadPage() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = SimplerUi.Loading) }

        when (val result = getEpisodeDetailsUC(seriesId, seasonNumber, episodeNumber)) {
            is ResultHandler.Error -> _uiState.update {
                it.copy(uiState = SimplerUi.Error(result.exception.messageResource))
            }

            is ResultHandler.Success -> _uiState.update {
                it.copy(details = result.data, uiState = SimplerUi.Idle)
            }
        }
    }
}
