package com.enmanuelbergling.ktormovies.data.source.preferences.domain

import com.enmanuelbergling.ktormovies.domain.model.settings.DarkTheme
import kotlinx.coroutines.flow.Flow

interface PreferencesDS {
    fun getDarkTheme(): Flow<DarkTheme>

    fun setDarkTheme(theme: DarkTheme)
}