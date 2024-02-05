package com.enmanuelbergling.ktormovies.ui.screen.login.model

sealed interface LoginEvent {
    data class Username(val value: String) : LoginEvent
    data class Password(val value: String) : LoginEvent
    data object PasswordVisibility : LoginEvent
    data object Submit : LoginEvent
}