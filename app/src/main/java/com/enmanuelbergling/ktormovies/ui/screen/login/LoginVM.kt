package com.enmanuelbergling.ktormovies.ui.screen.login

import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.domain.usecase.form.BasicFormValidationUC
import com.enmanuelbergling.ktormovies.ui.components.messageResource
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginChain
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginEvent
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

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