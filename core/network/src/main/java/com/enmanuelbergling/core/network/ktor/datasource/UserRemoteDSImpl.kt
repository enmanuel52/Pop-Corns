package com.enmanuelbergling.core.network.ktor.datasource

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.user.CreateListPost
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.model.user.WatchResponse
import com.enmanuelbergling.core.network.BuildConfig
import com.enmanuelbergling.core.network.dto.user.watch.FavoriteBody
import com.enmanuelbergling.core.network.dto.user.watch.MediaOnListBody
import com.enmanuelbergling.core.network.dto.user.watch.WatchlistBody
import com.enmanuelbergling.core.network.ktor.service.UserService
import com.enmanuelbergling.core.network.mappers.asBody
import com.enmanuelbergling.core.network.mappers.toModel
import kotlinx.coroutines.flow.firstOrNull

class UserRemoteDSImpl(
    private val service: UserService,
    private val authPreferenceDS: AuthPreferenceDS,
) : UserRemoteDS {

    private suspend fun getSessionId() = authPreferenceDS.getSessionId().firstOrNull()

    override suspend fun getAccount(): ResultHandler<UserDetails> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.getAccount(sessionId).toModel()
        }
    }

    override suspend fun createWatchList(
        listPost: CreateListPost,
    ): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall { service.createWatchList(listPost.asBody(), sessionId).toModel() }
    }

    override suspend fun deleteMovieFromList(
        movieId: Int,
        listId: Int,
    ): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.deleteMovieFromList(
                mediaBody = MediaOnListBody(movieId),
                listId = listId,
                sessionId = sessionId
            ).toModel()
        }
    }

    override suspend fun addMovieToList(
        movieId: Int,
        listId: Int,
    ): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.addMovieToList(
                mediaBody = MediaOnListBody(movieId),
                listId = listId,
                sessionId = sessionId
            ).toModel()
        }
    }

    override suspend fun deleteList(
        listId: Int,
    ): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.deleteList(
                listId = listId,
                sessionId = sessionId
            ).toModel()
        }
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
    ): ResultHandler<PageModel<WatchList>> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            val result = service.getAccountLists(accountId, sessionId, page)
            val movies = result.results.map { it.toModel() }

            PageModel(movies, result.totalPages)
        }
    }

    override suspend fun getAccountWatchlistMovies(
        page: Int,
    ): ResultHandler<PageModel<Movie>> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            val result = service.getWatchlistMovies(BuildConfig.ACCOUNT_ID, sessionId, page)
            val movies = result.results.map { it.toModel() }

            PageModel(movies, result.totalPages)
        }
    }

    override suspend fun addMovieToAccountWatchlist(
        movieId: Int,
    ): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.addToWatchlist(
                accountId = BuildConfig.ACCOUNT_ID,
                sessionId = sessionId,
                watchlistBody = WatchlistBody(mediaId = movieId, watchlist = true)
            ).toModel()
        }
    }

    override suspend fun removeMovieFromAccountWatchlist(
        movieId: Int,
    ): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.addToWatchlist(
                accountId = BuildConfig.ACCOUNT_ID,
                sessionId = sessionId,
                watchlistBody = WatchlistBody(mediaId = movieId, watchlist = false)
            ).toModel()
        }
    }

    override suspend fun getAccountFavoriteMovies(
        page: Int,
    ): ResultHandler<PageModel<Movie>> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            val result = service.getFavoriteMovies(BuildConfig.ACCOUNT_ID, sessionId, page)
            val movies = result.results.map { it.toModel() }

            PageModel(movies, result.totalPages)
        }
    }

    override suspend fun addMovieToFavorites(
        movieId: Int,
    ): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.addToFavorites(
                accountId = BuildConfig.ACCOUNT_ID,
                sessionId = sessionId,
                favoriteBody = FavoriteBody(mediaId = movieId, favorite = true)
            ).toModel()
        }
    }

    override suspend fun removeMovieFromFavorites(
        movieId: Int,
    ): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.addToFavorites(
                accountId = BuildConfig.ACCOUNT_ID,
                sessionId = sessionId,
                favoriteBody = FavoriteBody(mediaId = movieId, favorite = false)
            ).toModel()
        }
    }
}
