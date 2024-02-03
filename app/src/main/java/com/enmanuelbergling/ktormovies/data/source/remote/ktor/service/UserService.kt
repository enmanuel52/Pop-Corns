package com.enmanuelbergling.ktormovies.data.source.remote.ktor.service

import com.enmanuelbergling.ktormovies.BuildConfig
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.UserDetailsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class UserService(private val httpClient: KtorClient) {

    internal suspend fun getAccount(sessionId: String): UserDetailsDTO = httpClient
        .get("account/${BuildConfig.ACCOUNT_ID}") {
            url {
                parameters.append("session_id", sessionId)
            }
        }
        .body()
}