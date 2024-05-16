package com.enmanuelbergling.core.domain.datasource.remote

import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.user.CreateListPost
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchList
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

     suspend fun getWatchListMovies(
        listId: Int,
        page: Int,
    ): ResultHandler<PageModel<Movie>>

    suspend fun getWatchLists(
        accountId: String,
        sessionId: String,
        page: Int,
    ): ResultHandler<PageModel<WatchList>>
}