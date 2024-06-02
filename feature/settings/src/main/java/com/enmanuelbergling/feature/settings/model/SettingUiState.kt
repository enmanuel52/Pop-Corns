package com.enmanuelbergling.feature.settings.model

internal data class SettingUiState(
    val userDetails: UserUi?,
    val darkTheme: DarkThemeUi,
    val dynamicColor: Boolean,
    val darkThemeMenuOpen: Boolean,
    val dynamicThemeMenuOpen: Boolean,
)


