package com.enmanuelbergling.ktormovies.domain.usecase.auth

import com.enmanuelbergling.ktormovies.data.source.remote.domain.AuthRemoteDS
import com.enmanuelbergling.ktormovies.domain.model.auth.CreateSessionPost

class CreateSessionFromLoginUC(private val remoteDS: AuthRemoteDS) {

    /**
     *@param sessionPost validate a request token by entering it
     * the request token won´t change*/
    suspend operator fun invoke(sessionPost: CreateSessionPost) =
        remoteDS.createSessionFromLogin(sessionPost)
}