package com.enmanuelbergling.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.form.BasicFormValidationUC
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.auth.model.LoginChain
import com.enmanuelbergling.feature.auth.model.LoginChainHandler
import com.enmanuelbergling.feature.auth.model.LoginEvent
import com.enmanuelbergling.feature.auth.model.LoginForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginVM(
    private val loginChainHandler: LoginChainHandler,
    private val basicFormValidationUC: BasicFormValidationUC,
) : ViewModel() {

    private val _loginFormState = MutableStateFlow(LoginForm())
    val loginFormState get() = _loginFormState.asStateFlow()

    private val _loginChainState = MutableStateFlow(LoginChain())

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    fun onLoginFormEvent(event: LoginEvent) {
        _loginFormState.update {
            when (event) {
                is LoginEvent.Password -> it.copy(password = event.value, passwordError = null)
                LoginEvent.PasswordVisibility -> it.copy(isPasswordVisible = !it.isPasswordVisible)
                LoginEvent.Submit -> it.also {
                    if (validateFields()) login()
                }

                is LoginEvent.Username -> it.copy(username = event.value, usernameError = null)
            }
        }
    }

    private fun validateFields(): Boolean =
        with(_loginFormState) {
            val usernameValidation = basicFormValidationUC(value.username)
            val passwordValidation = basicFormValidationUC(value.password)

            update {
                it.copy(
                    usernameError = usernameValidation.errorMessage,
                    passwordError = passwordValidation.errorMessage
                )
            }

            usernameValidation.isSuccess && passwordValidation.isSuccess
        }


    private fun login() = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        _loginChainState.update { loginFormState.value.toLoginChain() }

        runCatching {
            loginChainHandler(
                _loginChainState
            )
        }.onFailure {
            _uiState.update { SimplerUi.Error(NetworkException.DefaultException.messageResource) }
        }.onSuccess {
            _uiState.update { SimplerUi.Success }
        }
    }

    fun onIdle() {
        _uiState.update { SimplerUi.Idle }
    }
}