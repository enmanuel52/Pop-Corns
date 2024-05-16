package com.enmanuelbergling.core.network.ktor.datasource

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.user.CreateListPost
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.model.user.WatchResponse
import com.enmanuelbergling.core.network.dto.user.watch.MediaOnListBody
import com.enmanuelbergling.core.network.ktor.service.UserService
import com.enmanuelbergling.core.network.mappers.asBody
import com.enmanuelbergling.core.network.mappers.toModel

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

    override suspend fun getWatchListMovies(listId: Int, page: Int) = safeKtorCall {
        val result = service.getListDetails(listId, page)
        val movies = result.items.map { it.toModel() }

        PageModel(movies, result.itemCount)
    }

    override suspend fun getWatchLists(
        accountId: String,
        sessionId: String,
        page: Int,
    ): ResultHandler<PageModel<WatchList>> = safeKtorCall {
        val result = service.getAccountLists(accountId, sessionId, page)
        val movies = result.results.map { it.toModel() }

        PageModel(movies, result.totalPages)
    }
}