package com.enmanuelbergling.core.network.ktor.service

import com.enmanuelbergling.core.network.dto.auth.CreateSessionBody
import com.enmanuelbergling.core.network.dto.auth.RequestTokenBody
import com.enmanuelbergling.core.network.dto.auth.RequestTokenDTO
import com.enmanuelbergling.core.network.dto.auth.SessionDTO
import com.enmanuelbergling.core.network.ktor.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI

@OptIn(InternalAPI::class)
internal class AuthService(private val httpClient: KtorClient) {
    /**
     * Create an intermediate request token that can be used to validate a user login
     * */
    suspend fun createRequestToken(): RequestTokenDTO = httpClient
        .get("authentication/token/new") {}
        .body()

    /**
     *@param sessionBody validate a request token by entering it
     * the request token won't change*/
    suspend fun createSessionFromLogin(sessionBody: CreateSessionBody): RequestTokenDTO = httpClient
        .post("authentication/token/validate_with_login") {
            contentType(ContentType.Application.Json)
            setBody(sessionBody)
        }
        .body()

    suspend fun createSessionId(requestTokenBody: RequestTokenBody): SessionDTO = httpClient
        .post("authentication/session/new") {
            contentType(ContentType.Application.Json)
            setBody(requestTokenBody)
        }
        .body()
}