package com.enmanuelbergling.core.domain.usecase.auth

import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS

class CreateRequestTokenUC(
    private val remoteDS: AuthRemoteDS
) {
    /**
     * Create an intermediate request token that can be used to validate a user login
     * */
    suspend operator fun invoke() = remoteDS.createRequestToken()
}