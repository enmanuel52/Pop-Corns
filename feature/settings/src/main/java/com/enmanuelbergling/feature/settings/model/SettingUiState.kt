package com.enmanuelbergling.feature.settings.model

import com.enmanuelbergling.core.model.settings.DarkTheme
import com.enmanuelbergling.core.model.user.UserDetails

data class SettingUiState(
    val userDetails: UserDetails,
    val darkTheme: DarkTheme,
    val dynamicTheme: Boolean,
    val darkThemeMenuOpen: Boolean,
    val dynamicThemeMenuOpen: Boolean,
)
