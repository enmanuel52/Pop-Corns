package com.enmanuelbergling.core.domain.datasource.preferences

import kotlinx.coroutines.flow.Flow

interface AuthPreferenceDS {
    fun saveSessionId(sessionId: String)

    fun getSessionId(): Flow<String>

    fun clear()
}