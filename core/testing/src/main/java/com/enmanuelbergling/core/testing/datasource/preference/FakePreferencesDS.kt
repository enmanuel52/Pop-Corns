package com.enmanuelbergling.core.testing.datasource.preference

import com.enmanuelbergling.core.domain.datasource.preferences.PreferencesDS
import com.enmanuelbergling.core.model.settings.DarkTheme
import kotlinx.coroutines.flow.flowOf

class FakePreferencesDS : PreferencesDS {
    private var _darkTheme = DarkTheme.System
    override fun getDarkTheme() = flowOf(_darkTheme)

    override fun setDarkTheme(theme: DarkTheme) {
        _darkTheme = theme
    }
}