package com.enmanuelbergling.feature.auth.model

data class LoginForm(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
){
    fun toLoginChain() = LoginChain(username, password)

    val hasAnyError: Boolean get() = usernameError != null || passwordError != null
}
