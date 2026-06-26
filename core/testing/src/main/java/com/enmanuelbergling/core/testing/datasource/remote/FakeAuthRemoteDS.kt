package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.model.RequestToken
import com.enmanuelbergling.core.model.SessionId
import com.enmanuelbergling.core.model.auth.CreateSessionPost
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler

enum class AuthRemoteDsFunction {
    CreateRequestToken,
    CreateSessionFromLogin,
    CreateSessionId
}

class FakeAuthRemoteDS : AuthRemoteDS {

    private val errors = mutableMapOf<AuthRemoteDsFunction, NetworkException>()

    fun throwError(vararg errors: Pair<AuthRemoteDsFunction, NetworkException>) {
        this.errors.putAll(errors)
    }

    private fun <T> checkError(function: AuthRemoteDsFunction): ResultHandler<T>? =
        errors[function]?.let { ResultHandler.Error(it) }

    override suspend fun createRequestToken(): ResultHandler<RequestToken> =
        checkError(AuthRemoteDsFunction.CreateRequestToken) ?: ResultHandler.Success("new token")

    override suspend fun createSessionFromLogin(sessionPost: CreateSessionPost): ResultHandler<RequestToken> =
        checkError(AuthRemoteDsFunction.CreateSessionFromLogin) ?: ResultHandler.Success("latest token")

    override suspend fun createSessionId(token: RequestToken): ResultHandler<SessionId> =
        checkError(AuthRemoteDsFunction.CreateSessionId) ?: ResultHandler.Success("new session id")
}
