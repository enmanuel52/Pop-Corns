package com.enmanuelbergling.ktormovies.domain.usecase.user

import com.enmanuelbergling.ktormovies.data.source.preferences.domain.AuthPreferenceDS
import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS

class GetUserDetailsUC(
    private val remoteDS: UserRemoteDS,
    private val preferenceDS: AuthPreferenceDS,
) {
    suspend operator fun invoke() =
        run {
            val sessionId = preferenceDS.getSessionId()

            remoteDS.getAccount(sessionId)
        }
}