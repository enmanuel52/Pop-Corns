package com.enmanuelbergling.feature.auth.datasource

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

object FakeAuthPreferenceDS: AuthPreferenceDS {
    private var _sessionId = ""
    override fun saveSessionId(sessionId: String) {
        _sessionId = sessionId
    }

    override fun getSessionId(): Flow<String> = flowOf(_sessionId)

    override fun clear() {
        _sessionId = ""
    }
}