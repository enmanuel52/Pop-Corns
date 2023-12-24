package com.enmanuelbergling.ktormovies.ui.screen.actor.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.ktormovies.domain.model.actor.ActorDetails
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.domain.usecase.GetActorDetailsUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActorDetailsVM(
    private val getActorDetailsUC: GetActorDetailsUC
): ViewModel() {

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    private val _detailsState = MutableStateFlow<ActorDetails?>(null)
    val detailsState get() = _detailsState.asStateFlow()

    fun getDetails(id: Int) = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        when (val result = getActorDetailsUC(id)) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.message.orEmpty()) }
            is ResultHandler.Success -> {
                _detailsState.update { result.data }
                _uiState.update { SimplerUi.Success }
            }
        }
    }
}