package com.enmanuelbergling.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.enmanuelbergling.core.domain.datasource.preferences.PreferencesDS
import com.enmanuelbergling.core.model.settings.DarkTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class PreferencesDSImpl(private val context: Context) : PreferencesDS {

    private val Context.dataStore by preferencesDataStore("settings")

    private object Keys {
        val THEME = stringPreferencesKey("theme")
    }

    override fun getDarkTheme(): Flow<DarkTheme> = context.dataStore.data.map { preferences ->
        preferences[Keys.THEME]?.let { it -> DarkTheme.valueOf(it) } ?: DarkTheme.System
    }

    override fun setDarkTheme(theme: DarkTheme): Unit = runBlocking {
        context.dataStore.edit { preferences ->
            preferences[Keys.THEME] = theme.toString()
        }
    }
}