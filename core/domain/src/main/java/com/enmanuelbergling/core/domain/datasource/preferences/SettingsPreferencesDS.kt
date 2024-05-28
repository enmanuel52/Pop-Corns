package com.enmanuelbergling.core.domain.datasource.preferences

import com.enmanuelbergling.core.model.settings.DarkTheme
import kotlinx.coroutines.flow.Flow

interface SettingsPreferencesDS {
    fun getDarkTheme(): Flow<DarkTheme>

    fun setDarkTheme(theme: DarkTheme)
    fun getDynamicTheme(): Flow<Boolean>

    fun setDynamicTheme(active: Boolean)
}