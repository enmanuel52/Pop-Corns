package com.enmanuelbergling.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getSessionId(): Flow<String> =
        context.dataStore.data.map { it[Keys.SESSION_ID].orEmpty() }

    override fun clear() {
        saveSessionId("")
    }
}