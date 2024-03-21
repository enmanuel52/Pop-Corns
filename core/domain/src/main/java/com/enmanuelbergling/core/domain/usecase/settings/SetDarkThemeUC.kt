package com.enmanuelbergling.core.domain.usecase.settings

import com.enmanuelbergling.core.domain.datasource.preferences.PreferencesDS
import com.enmanuelbergling.core.model.settings.DarkTheme

class SetDarkThemeUC(private val localDS: PreferencesDS) {
    operator fun invoke(theme: DarkTheme) = localDS.setDarkTheme(theme)
}