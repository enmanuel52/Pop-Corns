package com.enmanuelbergling.ktormovies.domain.usecase.auth

import com.enmanuelbergling.ktormovies.data.source.preferences.domain.AuthPreferenceDS
import com.enmanuelbergling.ktormovies.data.source.remote.domain.AuthRemoteDS
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler


class CreateSessionIdUC(private val remoteDS: AuthRemoteDS, private val localDS: AuthPreferenceDS) {
    /**
     * get sessionId that is required for user related data requests
     * and save it in preferences
     * */
    suspend operator fun invoke(token: String) = remoteDS.createSessionId(token).also {
        if (it is ResultHandler.Success) {
            val sessionId = it.data.orEmpty()
            localDS.saveSessionId(sessionId)
        }
    }
}