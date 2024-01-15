package com.enmanuelbergling.ktormovies.ui.screen.actor.details

import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.ui.screen.actor.details.model.ActorDetailsChainStart
import com.enmanuelbergling.ktormovies.ui.screen.actor.details.model.ActorDetailsUiData
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
            _uiState.update { SimplerUi.Error(throwable.message.orEmpty()) }
        }.onSuccess {
            _uiState.update { SimplerUi.Idle }
        }
    }
}