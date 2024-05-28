package com.enmanuelbergling.core.domain.usecase.settings

import com.enmanuelbergling.core.domain.datasource.preferences.SettingsPreferencesDS

class SetDynamicThemeUC(
    private val preferencesDS: SettingsPreferencesDS,
) {
    operator fun invoke(active: Boolean) {
        preferencesDS.setDynamicTheme(active)
    }
}