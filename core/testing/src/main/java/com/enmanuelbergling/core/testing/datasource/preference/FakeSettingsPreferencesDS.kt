package com.enmanuelbergling.core.testing.datasource.preference

import com.enmanuelbergling.core.domain.datasource.preferences.SettingsPreferencesDS
import com.enmanuelbergling.core.model.settings.DarkTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSettingsPreferencesDS : SettingsPreferencesDS {
    private var _darkTheme = DarkTheme.System
    private var _dynamicTheme = false
    override fun getDarkTheme() = flowOf(_darkTheme)

    override fun setDarkTheme(theme: DarkTheme) {
        _darkTheme = theme
    }

    override fun getDynamicColor(): Flow<Boolean> = flowOf(_dynamicTheme)

    override fun setDynamicColor(active: Boolean) {
        _dynamicTheme = active
    }
}