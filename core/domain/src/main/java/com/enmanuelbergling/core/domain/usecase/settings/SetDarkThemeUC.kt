package com.enmanuelbergling.core.domain.usecase.settings

import com.enmanuelbergling.core.domain.datasource.preferences.SettingsPreferencesDS
import com.enmanuelbergling.core.model.settings.DarkTheme

class SetDarkThemeUC(private val localDS: SettingsPreferencesDS) {
    operator fun invoke(theme: DarkTheme) = localDS.setDarkTheme(theme)
}