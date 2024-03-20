package com.enmanuelbergling.ktormovies.domain.usecase.settings

import com.enmanuelbergling.ktormovies.data.source.preferences.domain.PreferencesDS
import com.enmanuelbergling.core.model.settings.DarkTheme

class SetDarkThemeUC(private val localDS: PreferencesDS) {
    operator fun invoke(theme: DarkTheme) = localDS.setDarkTheme(theme)
}