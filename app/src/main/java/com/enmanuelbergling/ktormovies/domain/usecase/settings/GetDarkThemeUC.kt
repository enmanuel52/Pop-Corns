package com.enmanuelbergling.ktormovies.domain.usecase.settings

import com.enmanuelbergling.ktormovies.data.source.preferences.domain.PreferencesDS

class GetDarkThemeUC(private val localDS: PreferencesDS) {
    operator fun invoke() = localDS.getDarkTheme()
}