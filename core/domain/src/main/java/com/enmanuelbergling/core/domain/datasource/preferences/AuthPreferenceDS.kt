package com.enmanuelbergling.core.domain.datasource.preferences

import kotlinx.coroutines.flow.Flow

interface AuthPreferenceDS {
    suspend fun saveSessionId(sessionId: String)

    fun getSessionId(): Flow<String>

    suspend fun clear()
}