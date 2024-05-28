package com.enmanuelbergling.core.domain.usecase.settings

import com.enmanuelbergling.core.domain.datasource.preferences.SettingsPreferencesDS

class GetDynamicColorUC(
    private val preferencesDS: SettingsPreferencesDS
) {
     operator fun invoke()=preferencesDS.getDynamicColor()
}