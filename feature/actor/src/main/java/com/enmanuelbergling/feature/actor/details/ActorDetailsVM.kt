package com.enmanuelbergling.feature.actor.details

import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsChainStart
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class ActorDetailsVM(
    actorId: Int = 0,
    private val actorDetailsChainStart: ActorDetailsChainStart,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiDataState = MutableStateFlow(ActorDetailsUiData(actorId = actorId))
    val uiDataState get() = _uiDataState.asStateFlow()

    init {
        loadPage()
    }

    fun loadPage() = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        runCatching {
            actorDetailsChainStart.invoke(_uiDataState)
        }.onFailure { throwable ->
            _uiState.update { SimplerUi.Error(NetworkException.DefaultException.messageResource) }
        }.onSuccess {
            _uiState.update { SimplerUi.Idle }
        }
    }
}