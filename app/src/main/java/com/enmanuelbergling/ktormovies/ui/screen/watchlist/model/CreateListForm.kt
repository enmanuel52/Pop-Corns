package com.enmanuelbergling.ktormovies.ui.screen.watchlist.model

import com.enmanuelbergling.ktormovies.domain.model.user.CreateListPost

data class CreateListForm(
    val name: String = "",
    val nameError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val isVisible: Boolean = false,
) {
    fun toPost() = CreateListPost(name, description)
}
