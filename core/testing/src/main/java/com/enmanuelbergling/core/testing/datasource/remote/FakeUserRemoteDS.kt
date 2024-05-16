package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
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

/**
 * Watch list operation are made against a single one
 * */
class FakeUserRemoteDS(
    private val userResponse: UserDetails = UserDetails(),
) : UserRemoteDS {
    private var _watchList: WatchList? = null

    private val _watchListMovieIds = mutableListOf<MovieId>()

    override suspend fun getAccount(sessionId: String): ResultHandler<UserDetails> =
        ResultHandler.Success(userResponse)

    override suspend fun createWatchList(
        listPost: CreateListPost,
        sessionId: String,
    ) = ResultHandler.Success(DEFAULT_WATCH_RESPONSE).also {
        _watchList = EMPTY_WATCH_LIST.copy(
            description = listPost.description,
            name = listPost.name
        )
    }

    override suspend fun deleteMovieFromList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ) = ResultHandler.Success(DEFAULT_WATCH_RESPONSE).also {
        if (_watchList != null) {
            _watchListMovieIds.remove(movieId)
        }
    }

    override suspend fun addMovieToList(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ) = ResultHandler.Success(DEFAULT_WATCH_RESPONSE).also {
        if (_watchList != null) {
            _watchListMovieIds.add(movieId)
        }
    }

    override suspend fun deleteList(listId: Int, sessionId: String) =
        ResultHandler.Success(DEFAULT_WATCH_RESPONSE).also {
            _watchList = null
            _watchListMovieIds.clear()
        }

    override suspend fun checkItemStatus(listId: Int, movieId: Int): ResultHandler<Boolean> =
        ResultHandler.Success(_watchListMovieIds.any { id -> id == movieId })

    override suspend fun getWatchListMovies(listId: Int, page: Int): ResultHandler<PageModel<Movie>> =
        ResultHandler.Success(
            _watchListMovieIds.map { movieId ->
                FakeMovieData.MOVIES.first().copy(id = movieId)
            }.asPage()
        )

    override suspend fun getWatchLists(
        accountId: String,
        sessionId: String,
        page: Int,
    ): ResultHandler<PageModel<WatchList>> =
        if (_watchList != null) {
            ResultHandler.Success(listOf(_watchList!!).asPage())
        } else {
            ResultHandler.Success(emptyList<WatchList>().asPage())
        }

}