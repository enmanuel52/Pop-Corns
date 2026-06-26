package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.model.RequestToken
import com.enmanuelbergling.core.model.SessionId
import com.enmanuelbergling.core.model.auth.CreateSessionPost
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler

class FakeAuthRemoteDS : AuthRemoteDS {

    var errorToThrow: NetworkException? = null

    private fun <T> checkError(): ResultHandler<T>? = errorToThrow?.let { ResultHandler.Error(it) }

    override suspend fun createRequestToken(): ResultHandler<RequestToken> =
        checkError() ?: ResultHandler.Success("new token")

    override suspend fun createSessionFromLogin(sessionPost: CreateSessionPost): ResultHandler<RequestToken> =
        checkError() ?: ResultHandler.Success("latest token")

    override suspend fun createSessionId(token: RequestToken): ResultHandler<SessionId> =
        checkError() ?: ResultHandler.Success("new session id")
}
