package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.asPage
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.user.CreateListPost
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.model.user.WatchResponse
import com.enmanuelbergling.core.testing.data.FakeMovieData

val DEFAULT_WATCH_RESPONSE = WatchResponse("all went well")

val EMPTY_WATCH_LIST = WatchList(
    description = "",
    favoriteCount = 1,
    id = 0,
    itemCount = 1,
    iso6391 = "",
    listType = "",
    name = "",
    posterPath = null
)

typealias MovieId = Int

enum class UserRemoteDsFunction {
    GetAccount,
    CreateWatchList,
    DeleteMovieFromList,
    AddMovieToList,
    DeleteList,
    CheckItemStatus,
    GetWatchListMovies,
    GetWatchLists,
    GetAccountWatchlistMovies,
    AddMovieToAccountWatchlist,
    RemoveMovieFromAccountWatchlist,
    GetAccountFavoriteMovies,
    AddMovieToFavorites,
    RemoveMovieFromFavorites
}

/**
 * Watch list operation are made against a single one
 * */
class FakeUserRemoteDS(
    private val userResponse: UserDetails = UserDetails(),
) : UserRemoteDS {
    private var _watchList: WatchList? = null

    private val _watchListMovieIds = mutableListOf<MovieId>()

    private val errors = mutableMapOf<UserRemoteDsFunction, NetworkException>()

    fun throwError(vararg errors: Pair<UserRemoteDsFunction, NetworkException>) {
        this.errors.putAll(errors)
    }

    private fun <T> checkError(function: UserRemoteDsFunction): ResultHandler<T>? =
        errors[function]?.let { ResultHandler.Error(it) }

    override suspend fun getAccount(): ResultHandler<UserDetails> =
        checkError(UserRemoteDsFunction.GetAccount) ?: ResultHandler.Success(userResponse)

    override suspend fun createWatchList(
        listPost: CreateListPost,
    ) = checkError(UserRemoteDsFunction.CreateWatchList) ?: ResultHandler.Success(DEFAULT_WATCH_RESPONSE).also {
        _watchList = EMPTY_WATCH_LIST.copy(
            description = listPost.description,
            name = listPost.name
        )
    }

    override suspend fun deleteMovieFromList(
        movieId: Int,
        listId: Int,
    ) = checkError(UserRemoteDsFunction.DeleteMovieFromList) ?: ResultHandler.Success(DEFAULT_WATCH_RESPONSE).also {
        if (_watchList != null) {
            _watchListMovieIds.remove(movieId)
        }
    }

    override suspend fun addMovieToList(
        movieId: Int,
        listId: Int,
    ) = checkError(UserRemoteDsFunction.AddMovieToList) ?: ResultHandler.Success(DEFAULT_WATCH_RESPONSE).also {
        if (_watchList != null) {
            _watchListMovieIds.add(movieId)
        }
    }

    override suspend fun deleteList(listId: Int) =
        checkError(UserRemoteDsFunction.DeleteList) ?: ResultHandler.Success(DEFAULT_WATCH_RESPONSE).also {
            _watchList = null
            _watchListMovieIds.clear()
        }

    override suspend fun checkItemStatus(listId: Int, movieId: Int): ResultHandler<Boolean> =
        checkError(UserRemoteDsFunction.CheckItemStatus) ?: ResultHandler.Success(_watchListMovieIds.any { id -> id == movieId })

    override suspend fun getWatchListMovies(listId: Int, page: Int): ResultHandler<PageModel<Movie>> =
        checkError(UserRemoteDsFunction.GetWatchListMovies) ?: ResultHandler.Success(
            _watchListMovieIds.map { movieId ->
                FakeMovieData.MOVIES.first().copy(id = movieId)
            }.asPage()
        )

    override suspend fun getWatchLists(
        accountId: String,
        page: Int,
    ): ResultHandler<PageModel<WatchList>> =
        checkError(UserRemoteDsFunction.GetWatchLists) ?: if (_watchList != null) {
            ResultHandler.Success(listOf(_watchList!!).asPage())
        } else {
            ResultHandler.Success(emptyList<WatchList>().asPage())
        }

    override suspend fun getAccountWatchlistMovies(
        page: Int,
    ): ResultHandler<PageModel<Movie>> =
        checkError(UserRemoteDsFunction.GetAccountWatchlistMovies) ?: ResultHandler.Success(emptyList<Movie>().asPage())

    override suspend fun addMovieToAccountWatchlist(
        movieId: Int,
    ): ResultHandler<WatchResponse> {
        return checkError(UserRemoteDsFunction.AddMovieToAccountWatchlist) ?: ResultHandler.Success(
            DEFAULT_WATCH_RESPONSE
        )
    }

    override suspend fun removeMovieFromAccountWatchlist(
        movieId: Int,
    ): ResultHandler<WatchResponse> {
        return checkError(UserRemoteDsFunction.RemoveMovieFromAccountWatchlist)
            ?: ResultHandler.Success(DEFAULT_WATCH_RESPONSE)
    }

    override suspend fun getAccountFavoriteMovies(page: Int): ResultHandler<PageModel<Movie>> =
        checkError(UserRemoteDsFunction.GetAccountFavoriteMovies) ?: ResultHandler.Success(emptyList<Movie>().asPage())

    override suspend fun addMovieToFavorites(movieId: Int): ResultHandler<WatchResponse> {
        return checkError(UserRemoteDsFunction.AddMovieToFavorites) ?: ResultHandler.Success(
            DEFAULT_WATCH_RESPONSE
        )
    }

    override suspend fun removeMovieFromFavorites(movieId: Int): ResultHandler<WatchResponse> {
        return checkError(UserRemoteDsFunction.RemoveMovieFromFavorites) ?: ResultHandler.Success(
            DEFAULT_WATCH_RESPONSE
        )
    }
}
