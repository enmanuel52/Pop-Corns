package com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource

import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.MediaOnListBody
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.UserService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.asBody
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.user.CreateListPost
import com.enmanuelbergling.ktormovies.domain.model.user.UserDetails
import com.enmanuelbergling.ktormovies.domain.model.user.WatchResponse

class UserRemoteDSImpl(private val service: UserService) : UserRemoteDS {

    override suspend fun getAccount(sessionId: String): ResultHandler<UserDetails> = safeKtorCall {
        service.getAccount(sessionId).toModel()
    }

    override suspend fun createWatchList(
        listPost: CreateListPost,
        sessionId: String,
    ): ResultHandler<WatchResponse> =
        safeKtorCall { service.createWatchList(listPost.asBody(), sessionId).toModel() }

    override suspend fun deleteMovieFromList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ): ResultHandler<WatchResponse> = safeKtorCall {
        service.deleteMovieFromList(
            mediaBody = MediaOnListBody(movieId),
            listId = listId,
            sessionId = sessionId
        ).toModel()
    }

    override suspend fun addMovieToList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ): ResultHandler<WatchResponse> = safeKtorCall {
        service.addMovieToList(
            mediaBody = MediaOnListBody(movieId),
            listId = listId,
            sessionId = sessionId
        ).toModel()
    }

    override suspend fun deleteList(
        listId: Int,
        sessionId: String,
    ): ResultHandler<WatchResponse> = safeKtorCall {
        service.deleteList(
            listId = listId,
            sessionId = sessionId
        )
            .toModel()
    }

    override suspend fun checkItemStatus(listId: Int, movieId: Int): ResultHandler<Boolean> =
        safeKtorCall {
            service.checkItemStatus(listId, movieId).itemPresent
        }
}