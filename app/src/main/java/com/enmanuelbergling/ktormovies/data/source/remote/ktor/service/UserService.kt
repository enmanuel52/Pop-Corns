package com.enmanuelbergling.ktormovies.data.source.remote.ktor.service

import com.enmanuelbergling.ktormovies.BuildConfig
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.UserDetailsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.CreateListBody
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.MediaOnListBody
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.WatchResponseDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserService(private val httpClient: KtorClient) {

    internal suspend fun getAccount(sessionId: String): UserDetailsDTO = httpClient
        .get("account/${BuildConfig.ACCOUNT_ID}") {
            url {
                parameters.append("session_id", sessionId)
            }
        }
        .body()

    internal suspend fun createWatchList(
        listBody: CreateListBody,
        sessionId: String,
    ): WatchResponseDTO = httpClient
        .post("list") {
            url {
                parameters.append("session_id", sessionId)
            }
            contentType(ContentType.Application.Json)
            setBody(listBody)
        }
        .body()

    internal suspend fun deleteMovieFromList(
        mediaBody: MediaOnListBody,
        listId: Int,
        sessionId: String,
    ): WatchResponseDTO = httpClient
        .post("list/$listId/remove_item") {
            url {
                parameters.append("session_id", sessionId)
            }
            contentType(ContentType.Application.Json)
            setBody(mediaBody)
        }
        .body()

    internal suspend fun addMovieToList(
        mediaBody: MediaOnListBody,
        listId: Int,
        sessionId: String,
    ): WatchResponseDTO = httpClient
        .post("list/$listId/add_item") {
            url {
                parameters.append("session_id", sessionId)
            }
            contentType(ContentType.Application.Json)
            setBody(mediaBody)
        }
        .body()

    internal suspend fun deleteList(
        listId: Int,
        sessionId: String,
    ): WatchResponseDTO = httpClient
        .delete("list/$listId") {
            url {
                parameters.append("session_id", sessionId)
            }
        }
        .body()
}