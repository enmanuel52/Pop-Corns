package com.enmanuelbergling.feature.auth.model

sealed interface LoginEvent {
    data class Username(val value: String) : LoginEvent
    data class Password(val value: String) : LoginEvent
    data object PasswordVisibility : LoginEvent
    data object Submit : LoginEvent
}