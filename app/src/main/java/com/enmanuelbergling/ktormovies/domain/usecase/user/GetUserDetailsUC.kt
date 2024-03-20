package com.enmanuelbergling.ktormovies.domain.usecase.user

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.user.UserDetails
import kotlinx.coroutines.flow.firstOrNull

class GetUserDetailsUC(
    private val remoteDS: UserRemoteDS,
    private val preferenceDS: AuthPreferenceDS,
    private val userPreferenceDS: UserPreferenceDS,
) {
    suspend operator fun invoke(): ResultHandler<UserDetails> = run {
        val sessionId = preferenceDS.getSessionId().firstOrNull()
            ?: return ResultHandler.Error(NetworkException.AuthorizationException)

        remoteDS.getAccount(sessionId).also { result ->
            if (result is ResultHandler.Success) {
                result.data?.let { details -> userPreferenceDS.updateUser(details) }
            }
        }
    }
}