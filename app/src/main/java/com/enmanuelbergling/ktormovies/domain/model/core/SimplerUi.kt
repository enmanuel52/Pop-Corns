package com.enmanuelbergling.ktormovies.domain.model.core

import androidx.annotation.StringRes

sealed interface SimplerUi {
    data object Loading : SimplerUi
    data object Idle : SimplerUi
    data object Success : SimplerUi
    data class Error(@StringRes val messageRes: Int) : SimplerUi
}