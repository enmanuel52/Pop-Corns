package com.enmanuelbergling.feature.settings.model

import com.enmanuelbergling.core.model.settings.DarkTheme


sealed interface SettingUiEvent {
    data class DarkThemeEvent(val theme: DarkTheme) : SettingUiEvent
    data class DynamicTheme(val active: Boolean) : SettingUiEvent
    data object Logout : SettingUiEvent
    data object DarkThemeMenu : SettingUiEvent
    data object DynamicThemeMenu : SettingUiEvent
}