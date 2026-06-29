package com.enmanuelbergling.feature.tvshows.seasons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.tv.GetTvDetailsUC
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
    private val tvShowId: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeasonsState(tvShowId = tvShowId))
    val uiState = _uiState
        .onStart { loadPage() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SeasonsState(tvShowId = tvShowId)
        )

    private val _uiEvents = Channel<SeasonsEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onAction(action: SeasonsAction) {
        when (action) {
            SeasonsAction.OnBack -> viewModelScope.launch {
                _uiEvents.send(SeasonsEvent.NavigateBack)
            }

            SeasonsAction.OnRetry -> loadPage()
            is SeasonsAction.OnSeasonClick -> viewModelScope.launch {
                _uiEvents.send(SeasonsEvent.NavigateToEpisodes(action.seasonNumber))
            }

            is SeasonsAction.OnSeasonLongClick -> _uiState.update {
                it.copy(expandedSeasonId = action.seasonId.takeUnless { id -> id == it.expandedSeasonId })
            }
        }
    }

    private fun loadPage() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = SimplerUi.Loading) }

        when (val detailsResult = getTvDetailsUC(tvShowId)) {
            is ResultHandler.Error -> _uiState.update {
                it.copy(uiState = SimplerUi.Error(detailsResult.exception.messageResource))
            }

            is ResultHandler.Success -> _uiState.update {
                it.copy(details = detailsResult.data, uiState = SimplerUi.Idle)
            }
        }
    }
}
