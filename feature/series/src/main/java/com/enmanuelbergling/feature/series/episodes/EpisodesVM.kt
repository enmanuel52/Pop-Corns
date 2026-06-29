package com.enmanuelbergling.feature.series.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.tv.GetSeasonDetailsUC
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

internal class EpisodesVM(
    private val getSeasonDetailsUC: GetSeasonDetailsUC,
    private val seriesId: Int,
    private val seasonNumber: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EpisodesState(seriesId = seriesId, seasonNumber = seasonNumber)
    )
    val uiState = _uiState
        .onStart { loadPage() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EpisodesState(seriesId = seriesId, seasonNumber = seasonNumber)
        )

    private val _uiEvents = Channel<EpisodesEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onAction(action: EpisodesAction) {
        when (action) {
            EpisodesAction.OnBack -> viewModelScope.launch {
                _uiEvents.send(EpisodesEvent.NavigateBack)
            }

            EpisodesAction.OnRetry -> loadPage()
            is EpisodesAction.OnEpisodeClick -> viewModelScope.launch {
                _uiEvents.send(EpisodesEvent.NavigateToEpisodeDetails(action.episodeNumber))
            }
            is EpisodesAction.OnEpisodeLongClick -> _uiState.update {
                it.copy(expandedEpisodeId = action.episodeId.takeUnless { id -> id == it.expandedEpisodeId })
            }
        }
    }

    private fun loadPage() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = SimplerUi.Loading) }

        when (val result = getSeasonDetailsUC(seriesId, seasonNumber)) {
            is ResultHandler.Error -> _uiState.update {
                it.copy(uiState = SimplerUi.Error(result.exception.messageResource))
            }

            is ResultHandler.Success -> _uiState.update {
                it.copy(seasonDetails = result.data, uiState = SimplerUi.Idle)
            }
        }
    }
}
