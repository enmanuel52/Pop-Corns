package com.enmanuelbergling.ktormovies.data.source.preferences.domain

import kotlinx.coroutines.flow.Flow

interface AuthPreferenceDS {
    fun saveSessionId(sessionId: String)

    fun getSessionId(): Flow<String>

    fun clear()
}