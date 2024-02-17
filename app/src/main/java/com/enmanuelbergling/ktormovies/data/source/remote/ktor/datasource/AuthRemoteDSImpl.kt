package com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource

import com.enmanuelbergling.ktormovies.data.source.remote.domain.AuthRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.domain.RequestToken
import com.enmanuelbergling.ktormovies.data.source.remote.domain.SessionId
import com.enmanuelbergling.ktormovies.data.source.remote.dto.auth.RequestTokenBody
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.AuthService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.asBody
import com.enmanuelbergling.ktormovies.domain.model.auth.CreateSessionPost
import com.enmanuelbergling.ktormovies.domain.model.core.NetworkException
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

internal class AuthRemoteDSImpl(private val service: AuthService) : AuthRemoteDS {
    override suspend fun createRequestToken(): ResultHandler<RequestToken> = safeKtorCall {

        val result = service.createRequestToken()

        result.token
    }

    override suspend fun createSessionFromLogin(sessionPost: CreateSessionPost): ResultHandler<RequestToken> =
        safeKtorCall {

            val result = service.createSessionFromLogin(sessionPost.asBody())

            result.token
        }

    override suspend fun createSessionId(token: RequestToken): ResultHandler<SessionId> =
        safeKtorCall {

            val result = service.createSessionId(RequestTokenBody(token))

            result.sessionId
        }
}