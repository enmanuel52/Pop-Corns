package com.enmanuelbergling.ktormovies.domain.usecase.settings

import com.enmanuelbergling.core.domain.datasource.preferences.PreferencesDS

class GetDarkThemeUC(private val localDS: PreferencesDS) {
    operator fun invoke() = localDS.getDarkTheme()
}