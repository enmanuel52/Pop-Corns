package com.enmanuelbergling.core.domain.datasource.remote

import com.enmanuelbergling.core.model.RequestToken
import com.enmanuelbergling.core.model.SessionId
import com.enmanuelbergling.core.model.auth.CreateSessionPost
import com.enmanuelbergling.core.model.core.ResultHandler

interface AuthRemoteDS : RemoteDataSource {
    suspend fun createRequestToken(): ResultHandler<RequestToken>

    suspend fun createSessionFromLogin(sessionPost: CreateSessionPost): ResultHandler<RequestToken>

    suspend fun createSessionId(token: RequestToken): ResultHandler<SessionId>
}