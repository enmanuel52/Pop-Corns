package com.enmanuelbergling.ktormovies.ui.screen.login.model

data class LoginForm(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
){
    fun toLoginChain() = LoginChain(username, password)
}
