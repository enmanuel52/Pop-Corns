package com.enmanuelbergling.feature.auth.datasource

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.user.CreateListPost
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchResponse

class FakeUserRemoteDS(
    private val userDetails: UserDetails = UserDetails(),
) : UserRemoteDS {
    override suspend fun getAccount(sessionId: String): ResultHandler<UserDetails> =
        ResultHandler.Success(userDetails)

    override suspend fun createWatchList(
        listPost: CreateListPost,
        sessionId: String,
    ): ResultHandler<WatchResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMovieFromList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ): ResultHandler<WatchResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addMovieToList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ): ResultHandler<WatchResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteList(listId: Int, sessionId: String): ResultHandler<WatchResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun checkItemStatus(listId: Int, movieId: Int): ResultHandler<Boolean> {
        TODO("Not yet implemented")
    }
}