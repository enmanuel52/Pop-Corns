package com.enmanuelbergling.core.domain.usecase.settings

import com.enmanuelbergling.core.domain.datasource.preferences.SettingsPreferencesDS

class GetDarkThemeUC(private val localDS: SettingsPreferencesDS) {
    operator fun invoke() = localDS.getDarkTheme()
}