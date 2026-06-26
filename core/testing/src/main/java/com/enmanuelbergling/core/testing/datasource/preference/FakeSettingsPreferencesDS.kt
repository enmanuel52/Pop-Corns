package com.enmanuelbergling.core.testing.datasource.preference

import com.enmanuelbergling.core.domain.datasource.preferences.SettingsPreferencesDS
import com.enmanuelbergling.core.model.settings.DarkTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeSettingsPreferencesDS : SettingsPreferencesDS {
    private val _darkTheme = MutableStateFlow(DarkTheme.System)
    private val _dynamicTheme = MutableStateFlow(false)
    override fun getDarkTheme() = _darkTheme.asStateFlow()

    override fun setDarkTheme(theme: DarkTheme) {
        _darkTheme.update { theme }
    }

    override fun getDynamicColor(): Flow<Boolean> = _dynamicTheme.asStateFlow()

    override fun setDynamicColor(active: Boolean) {
        _dynamicTheme.update { active }
    }
}