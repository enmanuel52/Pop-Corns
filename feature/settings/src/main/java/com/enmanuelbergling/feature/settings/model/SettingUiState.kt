package com.enmanuelbergling.feature.settings.model

import com.enmanuelbergling.core.model.settings.DarkTheme
import com.enmanuelbergling.core.model.user.UserDetails

data class SettingUiState(
    val userDetails: UserUi,
    val darkTheme: DarkTheme,
    val dynamicColor: Boolean,
    val darkThemeMenuOpen: Boolean,
    val dynamicThemeMenuOpen: Boolean,
)

data class UserUi(
    val username: String = "",
    val avatarPath: String = "",
) {
    val isEmpty: Boolean get() = username.isBlank()
}

internal fun UserDetails.toSettingsUi() = UserUi(username, avatarPath)
