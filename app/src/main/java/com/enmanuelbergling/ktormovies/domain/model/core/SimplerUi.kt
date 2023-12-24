package com.enmanuelbergling.ktormovies.domain.model.core

sealed interface SimplerUi {
    data object Loading : SimplerUi
    data object Idle : SimplerUi
    data object Success : SimplerUi
    data class Error(val message: String) : SimplerUi
}