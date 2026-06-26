package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.asPage
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieAccountStates
import com.enmanuelbergling.core.testing.data.FakeMovieData

class FakeMovieRemoteDS : MovieRemoteDS {

    var errorToThrow: NetworkException? = null

    private fun <T> checkError(): ResultHandler<T>? = errorToThrow?.let { ResultHandler.Error(it) }

    override suspend fun getMovieDetails(id: Int) =
        checkError() ?: ResultHandler.Success(FakeMovieData.DEFAULT_MOVIE_DETAILS)

    override suspend fun getMovieCredits(id: Int) =
        checkError() ?: ResultHandler.Success(FakeMovieData.DEFAULT_MOVIE_CREDITS)

    override suspend fun getNowPlayingMovies(page: Int) =
        checkError() ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getUpcomingMovies(page: Int) =
        checkError() ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getTopRatedMovies(page: Int) =
        checkError() ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getPopularMovies(page: Int) =
        checkError() ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getMovieGenres() = checkError() ?: ResultHandler.Success(FakeMovieData.GENRES)

    override suspend fun getMoviesByGenre(
        genres: String,
        sortBy: String,
        page: Int,
    ): ResultHandler<PageModel<Movie>> = checkError() ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun searchMovie(query: String, page: Int): ResultHandler<PageModel<Movie>> =
        checkError() ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getMovieAccountStates(
        movieId: Int,
    ): ResultHandler<MovieAccountStates> =
        checkError() ?: ResultHandler.Success(MovieAccountStates(id = movieId, favorite = false, watchlist = false))
}
