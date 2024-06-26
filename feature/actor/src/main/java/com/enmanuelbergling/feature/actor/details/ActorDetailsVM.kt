package com.enmanuelbergling.feature.actor.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsChainStart
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActorDetailsVM(
    private val actorDetailsChainStart: ActorDetailsChainStart,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiDataState = MutableStateFlow(ActorDetailsUiData(actorId = 0))
    val uiDataState get() = _uiDataState.asStateFlow()

    fun loadPage(actorId: Int) = viewModelScope.launch {
        _uiDataState.update { ActorDetailsUiData(actorId = actorId) }
        _uiState.update { SimplerUi.Loading }
        runCatching {
            actorDetailsChainStart.invoke(_uiDataState)
        }.onFailure { _ ->
            _uiState.update { SimplerUi.Error(NetworkException.DefaultException.messageResource) }
        }.onSuccess {
            _uiState.update { SimplerUi.Idle }
        }
    }
}