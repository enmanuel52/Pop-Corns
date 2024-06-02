package com.enmanuelbergling.feature.settings.model

import com.enmanuelbergling.core.model.user.UserDetails

internal data class UserUi(
    val username: String = "",
    val avatarPath: String = "",
)

internal fun UserDetails.toSettingsUi() = UserUi(username, avatarPath)