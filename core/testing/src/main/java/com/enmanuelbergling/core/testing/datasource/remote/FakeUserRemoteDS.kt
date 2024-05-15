package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.user.CreateListPost
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchResponse

val DEFAULT_WATCH_RESPONSE = WatchResponse("all went well")

const val DEFAULT_MOVIE_IN_LIST = -20

class FakeUserRemoteDS(
    private val userResponse: UserDetails = UserDetails(),
) : UserRemoteDS {
    override suspend fun getAccount(sessionId: String): ResultHandler<UserDetails> =
        ResultHandler.Success(userResponse)

    override suspend fun createWatchList(
        listPost: CreateListPost,
        sessionId: String,
    ) = ResultHandler.Success(DEFAULT_WATCH_RESPONSE)

    override suspend fun deleteMovieFromList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ) = ResultHandler.Success(DEFAULT_WATCH_RESPONSE)

    override suspend fun addMovieToList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ) = ResultHandler.Success(DEFAULT_WATCH_RESPONSE)

    override suspend fun deleteList(listId: Int, sessionId: String) =
        ResultHandler.Success(DEFAULT_WATCH_RESPONSE)

    override suspend fun checkItemStatus(listId: Int, movieId: Int): ResultHandler<Boolean> =
        ResultHandler.Success(movieId == DEFAULT_MOVIE_IN_LIST)
}