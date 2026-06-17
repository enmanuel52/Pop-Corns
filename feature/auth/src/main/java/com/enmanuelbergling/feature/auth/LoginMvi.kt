package com.enmanuelbergling.feature.auth

import androidx.compose.runtime.Stable
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.feature.auth.model.LoginRequest

@Stable
data class LoginState(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val uiState: SimplerUi = SimplerUi.Idle,
)

fun LoginState.toLoginRequest() = LoginRequest(
    username = username,
    password = password
)

sealed interface LoginAction {
    data class OnUsernameChange(val value: String) : LoginAction
    data class OnPasswordChange(val value: String) : LoginAction
    data object OnPasswordVisibilityClick : LoginAction
    data object OnLoginClick : LoginAction
}

sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent
}
