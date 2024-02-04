package com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource

import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.DeleteMovieFromListBody
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
            deleteMovieBody = DeleteMovieFromListBody(movieId),
            listId = listId,
            sessionId = sessionId
        ).toModel()
    }
}