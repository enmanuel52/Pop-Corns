package com.enmanuelbergling.ktormovies.domain.usecase.auth

import com.enmanuelbergling.ktormovies.data.source.remote.domain.AuthRemoteDS

class CreateRequestTokenUC(
    private val remoteDS: AuthRemoteDS
) {
    /**
     * Create an intermediate request token that can be used to validate a user login
     * */
    suspend operator fun invoke() = remoteDS.createRequestToken()
}