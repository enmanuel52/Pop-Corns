package com.enmanuelbergling.ktormovies.domain.usecase.user

import com.enmanuelbergling.ktormovies.data.source.preferences.domain.AuthPreferenceDS
import com.enmanuelbergling.ktormovies.data.source.preferences.domain.UserPreferenceDS
import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.user.UserDetails
import kotlinx.coroutines.flow.firstOrNull

class GetUserDetailsUC(
    private val remoteDS: UserRemoteDS,
    private val preferenceDS: AuthPreferenceDS,
    private val userPreferenceDS: UserPreferenceDS,
) {
    suspend operator fun invoke(): ResultHandler<UserDetails> = run {
        val sessionId = preferenceDS.getSessionId().firstOrNull()
            ?: return ResultHandler.Error(Exception("Not session found, login and try again"))

        remoteDS.getAccount(sessionId).also { result ->
            if (result is ResultHandler.Success) {
                result.data?.let { details -> userPreferenceDS.updateUser(details) }
            }
        }
    }
}