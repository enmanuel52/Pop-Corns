package com.enmanuelbergling.feature.settings.model

import com.enmanuelbergling.core.model.settings.DarkTheme

internal enum class DarkThemeUi(val label: String) {
    No("Light"), Yes("Dark"), System("System");

    fun toModel() = when (this) {
        No -> DarkTheme.No
        Yes -> DarkTheme.Yes
        System -> DarkTheme.System
    }
}

internal fun DarkTheme.toSettingsUi() = when (this) {
    DarkTheme.No -> DarkThemeUi.No
    DarkTheme.Yes -> DarkThemeUi.Yes
    DarkTheme.System -> DarkThemeUi.System
}