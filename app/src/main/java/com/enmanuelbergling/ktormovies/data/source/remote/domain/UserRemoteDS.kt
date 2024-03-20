package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.user.CreateListPost
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchResponse

interface UserRemoteDS : RemoteDataSource {
    suspend fun getAccount(sessionId: String): ResultHandler<UserDetails>

    suspend fun createWatchList(
        listPost: CreateListPost,
        sessionId: String,
    ): ResultHandler<WatchResponse>

    suspend fun deleteMovieFromList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ): ResultHandler<WatchResponse>

    suspend fun addMovieToList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ): ResultHandler<WatchResponse>

    suspend fun deleteList(
        listId: Int,
        sessionId: String,
    ): ResultHandler<WatchResponse>

    suspend fun checkItemStatus(
        listId: Int,
        movieId: Int,
    ): ResultHandler<Boolean>
}