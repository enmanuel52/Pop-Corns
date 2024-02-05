package com.enmanuelbergling.ktormovies.domain.model.user

data class UserDetails(
    val id: Int = 0,
    val username: String = "",
    val avatarPath: String = "",
    val name: String = "",
)