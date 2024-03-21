package com.enmanuelbergling.core.domain.usecase.auth

import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.model.auth.CreateSessionPost

class CreateSessionFromLoginUC(private val remoteDS: AuthRemoteDS) {

    /**
     *@param sessionPost validate a request token by entering it
     * the request token won´t change*/
    suspend operator fun invoke(sessionPost: CreateSessionPost) =
        remoteDS.createSessionFromLogin(sessionPost)
}