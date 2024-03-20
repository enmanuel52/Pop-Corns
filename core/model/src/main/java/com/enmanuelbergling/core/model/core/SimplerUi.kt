package com.enmanuelbergling.core.model.core


sealed interface SimplerUi {
    data object Loading : SimplerUi
    data object Idle : SimplerUi
    data object Success : SimplerUi
    /**
     * @param messageRes must be a string resource
     * */
    data class Error(val messageRes: Int) : SimplerUi
}