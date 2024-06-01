package com.enmanuelbergling.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.enmanuelbergling.core.domain.datasource.preferences.SettingsPreferencesDS
import com.enmanuelbergling.core.model.settings.DarkTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class SettingsPreferencesDSImpl(private val context: Context) : SettingsPreferencesDS {

    private val Context.dataStore by preferencesDataStore("settings")

    private object Keys {
        val DARK_THEME = stringPreferencesKey("dark theme")
        val DYNAMIC_THEME = booleanPreferencesKey("dynamic theme")
    }

    override fun getDarkTheme(): Flow<DarkTheme> = context.dataStore.data.map { preferences ->
        preferences[Keys.DARK_THEME]?.let { it -> DarkTheme.valueOf(it) } ?: DarkTheme.System
    }

    override fun setDarkTheme(theme: DarkTheme): Unit = runBlocking {
        context.dataStore.edit { preferences ->
            preferences[Keys.DARK_THEME] = theme.toString()
        }
    }

    override fun getDynamicColor(): Flow<Boolean> =
        context.dataStore.data.map { preferences -> preferences[Keys.DYNAMIC_THEME] ?: false }

    override fun setDynamicColor(active: Boolean): Unit = runBlocking {
        context.dataStore.edit { preferences ->
            preferences[Keys.DYNAMIC_THEME] = active
        }
    }
}