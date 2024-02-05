package com.enmanuelbergling.ktormovies.data.source.preferences.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.enmanuelbergling.ktormovies.data.source.preferences.domain.AuthPreferenceDS
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class AuthPreferenceDSImpl(private val context: Context) : AuthPreferenceDS {

    private val Context.dataStore by preferencesDataStore("auth")

    private object Keys {
        val SESSION_ID = stringPreferencesKey("session_id")
    }

    override fun saveSessionId(sessionId: String): Unit = runBlocking {
        context.dataStore.edit {
            it[Keys.SESSION_ID] = sessionId
        }
    }

    override fun getSessionId(): String = runBlocking {
        context.dataStore.data.firstOrNull()?.let { it[Keys.SESSION_ID] } ?: ""
    }

    override fun clear() {
        saveSessionId("")
    }
}