package com.enmanuelbergling.core.testing.datasource.preference

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeAuthPreferenceDS: AuthPreferenceDS {
    private val _sessionId = MutableStateFlow("")
    override suspend fun saveSessionId(sessionId: String) {
        _sessionId.update { sessionId }
    }

    override fun getSessionId(): Flow<String> = _sessionId.asStateFlow()

    override suspend fun clear() {
        _sessionId.update { "" }
    }
}