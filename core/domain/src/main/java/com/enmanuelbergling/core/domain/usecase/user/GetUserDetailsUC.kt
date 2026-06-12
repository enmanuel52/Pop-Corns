package com.enmanuelbergling.core.domain.usecase.user

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.user.UserDetails

class GetUserDetailsUC(
    private val remoteDS: UserRemoteDS,
    private val userPreferenceDS: UserPreferenceDS,
) {
    suspend operator fun invoke(): ResultHandler<UserDetails> =
        remoteDS.getAccount().also { result ->
            if (result is ResultHandler.Success) {
                result.data?.let { details -> userPreferenceDS.updateUser(details) }
            }
        }
}
