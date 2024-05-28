package com.enmanuelbergling.core.model.user

data class UserDetails(
    val id: Int = 0,
    val username: String = "",
    val avatarPath: String = "",
    val name: String = "",
){
    val isEmpty: Boolean get() = username.isBlank()
}
