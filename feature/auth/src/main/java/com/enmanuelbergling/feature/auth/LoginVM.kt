package com.enmanuelbergling.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.form.BasicFormValidationUC
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.auth.model.LoginChain
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginVM(
    private val loginChain: LoginChain,
    private val basicFormValidationUC: BasicFormValidationUC,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = Channel<LoginEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnUsernameChange -> _uiState.update {
                it.copy(
                    username = action.value,
                    usernameError = null
                )
            }

            is LoginAction.OnPasswordChange -> _uiState.update {
                it.copy(
                    password = action.value,
                    passwordError = null
                )
            }

            LoginAction.OnPasswordVisibilityClick -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            LoginAction.OnLoginClick -> if (validateFields()) login()
        }
    }

    private fun validateFields(): Boolean {
        val usernameValidation = basicFormValidationUC(_uiState.value.username)
        val passwordValidation = basicFormValidationUC(_uiState.value.password)

        _uiState.update {
            it.copy(
                usernameError = usernameValidation.errorMessage,
                passwordError = passwordValidation.errorMessage
            )
        }

        return usernameValidation.isSuccess && passwordValidation.isSuccess
    }

    private fun login() = viewModelScope.launch {
        _uiState.update { it.copy(uiState = SimplerUi.Loading) }

        runCatching {
            val request = _uiState.value.toLoginRequest()

            val chain = loginChain.createRequestTokenHandler.apply {
                nextChainHandler = loginChain.createSessionFromLoginHandler.apply {
                    nextChainHandler = loginChain.createSessionIdHandler
                }
            }

            chain.invoke(request)
        }.onFailure {
            _uiState.update { it.copy(uiState = SimplerUi.Error(NetworkException.DefaultException.messageResource)) }
        }.onSuccess {
            _uiState.update { it.copy(uiState = SimplerUi.Idle) }
            _uiEvents.send(LoginEvent.LoginSuccess)
        }
    }

    fun onIdle() {
        _uiState.update { it.copy(uiState = SimplerUi.Idle) }
    }
}
