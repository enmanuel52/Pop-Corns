package com.enmanuelbergling.feature.settings.model

import com.enmanuelbergling.core.model.user.UserDetails

internal data class UserUi(
    val username: String = "",
    val avatarPath: String = "",
) {
    val isEmpty: Boolean get() = username.isBlank()
}

internal fun UserDetails.toSettingsUi() = UserUi(username, avatarPath)