package com.enmanuelbergling.ktormovies.data.source.preferences.domain

interface AuthPreferenceDS {
    fun saveSessionId(sessionId: String)

    fun getSessionId(): String
}