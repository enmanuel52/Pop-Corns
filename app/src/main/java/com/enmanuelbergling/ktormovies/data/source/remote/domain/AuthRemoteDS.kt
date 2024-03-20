package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.core.model.auth.CreateSessionPost
import com.enmanuelbergling.core.model.core.ResultHandler

typealias RequestToken = String
typealias SessionId = String
interface AuthRemoteDS: RemoteDataSource {
    suspend fun createRequestToken(): ResultHandler<RequestToken>

    suspend fun createSessionFromLogin(sessionPost: CreateSessionPost) : ResultHandler<RequestToken>

    suspend fun createSessionId(token: RequestToken): ResultHandler<SessionId>
}