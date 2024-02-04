package com.enmanuelbergling.ktormovies.ui.screen.login

import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginChain
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class LoginVM(
    private val loginChainHandler: LoginChainHandler,
) : ViewModel() {

    private val _loginFormState = MutableStateFlow(LoginForm())
    val loginFormState get() = _loginFormState.asStateFlow()

    private val _loginChainState = MutableStateFlow(LoginChain())

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    private fun login() = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        runCatching {
            _loginChainState.update { loginFormState.value.toLoginChain() }

            loginChainHandler(
                _loginChainState
            )
        }.onFailure {
            _uiState.update { SimplerUi.Error(it.toString()) }
        }.onSuccess {
            _uiState.update { SimplerUi.Success }
        }
    }
}