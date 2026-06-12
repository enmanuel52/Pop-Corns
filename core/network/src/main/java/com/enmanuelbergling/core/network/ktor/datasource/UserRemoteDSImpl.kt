package com.enmanuelbergling.core.network.ktor.datasource

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.user.CreateListPost
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.model.user.WatchResponse
import com.enmanuelbergling.core.network.BuildConfig
import com.enmanuelbergling.core.network.dto.user.watch.MediaOnListBody
import com.enmanuelbergling.core.network.dto.user.watch.WatchlistBody
import com.enmanuelbergling.core.network.ktor.service.UserService
import com.enmanuelbergling.core.network.mappers.asBody
import com.enmanuelbergling.core.network.mappers.toModel
import kotlinx.coroutines.flow.first

class UserRemoteDSImpl(
    private val service: UserService,
    private val authPreferenceDS: AuthPreferenceDS,
) : UserRemoteDS {

    private suspend fun getSessionId() = authPreferenceDS.getSessionId().first()

    override suspend fun getAccount(): ResultHandler<UserDetails> = safeKtorCall {
        service.getAccount(getSessionId()).toModel()
    }

    override suspend fun createWatchList(
        listPost: CreateListPost,
    ): ResultHandler<WatchResponse> =
        safeKtorCall { service.createWatchList(listPost.asBody(), getSessionId()).toModel() }

    override suspend fun deleteMovieFromList(
        movieId: Int,
        listId: Int,
    ): ResultHandler<WatchResponse> = safeKtorCall {
        service.deleteMovieFromList(
            mediaBody = MediaOnListBody(movieId),
            listId = listId,
            sessionId = getSessionId()
        ).toModel()
    }

    override suspend fun addMovieToList(
        movieId: Int,
        listId: Int,
    ): ResultHandler<WatchResponse> = safeKtorCall {
        service.addMovieToList(
            mediaBody = MediaOnListBody(movieId),
            listId = listId,
            sessionId = getSessionId()
        ).toModel()
    }

    override suspend fun deleteList(
        listId: Int,
    ): ResultHandler<WatchResponse> = safeKtorCall {
        service.deleteList(
            listId = listId,
            sessionId = getSessionId()
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
        page: Int,
    ): ResultHandler<PageModel<WatchList>> = safeKtorCall {
        val result = service.getAccountLists(accountId, getSessionId(), page)
        val movies = result.results.map { it.toModel() }

        PageModel(movies, result.totalPages)
    }

    override suspend fun getAccountWatchlistMovies(
        page: Int,
    ): ResultHandler<PageModel<Movie>> = safeKtorCall {
        val result = service.getWatchlistMovies(BuildConfig.ACCOUNT_ID, getSessionId(), page)
        val movies = result.results.map { it.toModel() }

        PageModel(movies, result.totalPages)
    }

    override suspend fun addMovieToAccountWatchlist(
        movieId: Int,
    ): ResultHandler<WatchResponse> = safeKtorCall {
        service.addToWatchlist(
            accountId = BuildConfig.ACCOUNT_ID,
            sessionId = getSessionId(),
            watchlistBody = WatchlistBody(mediaId = movieId, watchlist = true)
        ).toModel()
    }

    override suspend fun removeMovieFromAccountWatchlist(
        movieId: Int,
    ): ResultHandler<WatchResponse> = safeKtorCall {
        service.addToWatchlist(
            accountId = BuildConfig.ACCOUNT_ID,
            sessionId = getSessionId(),
            watchlistBody = WatchlistBody(mediaId = movieId, watchlist = false)
        ).toModel()
    }
}
