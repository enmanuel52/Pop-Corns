package com.enmanuelbergling.ktormovies.domain.usecase.auth

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.model.core.ResultHandler


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