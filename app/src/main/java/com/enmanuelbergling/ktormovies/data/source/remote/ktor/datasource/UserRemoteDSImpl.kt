package com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource

import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.UserService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.user.UserDetails

class UserRemoteDSImpl(private val service: UserService) : UserRemoteDS {

    override suspend fun getAccount(sessionId: String): ResultHandler<UserDetails> = safeKtorCall {
        service.getAccount(sessionId).toModel()
    }
}