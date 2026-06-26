package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.asPage
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieAccountStates
import com.enmanuelbergling.core.testing.data.FakeMovieData

enum class MovieRemoteDsFunction {
    GetMovieDetails,
    GetMovieCredits,
    GetNowPlayingMovies,
    GetUpcomingMovies,
    GetTopRatedMovies,
    GetPopularMovies,
    GetMovieGenres,
    GetMoviesByGenre,
    SearchMovie,
    GetMovieAccountStates
}

class FakeMovieRemoteDS : MovieRemoteDS {

    private val errors = mutableMapOf<MovieRemoteDsFunction, NetworkException>()

    fun throwError(vararg errors: Pair<MovieRemoteDsFunction, NetworkException>) {
        this.errors.putAll(errors)
    }

    private fun <T> checkError(function: MovieRemoteDsFunction): ResultHandler<T>? =
        errors[function]?.let { ResultHandler.Error(it) }

    override suspend fun getMovieDetails(id: Int) =
        checkError(MovieRemoteDsFunction.GetMovieDetails)
            ?: ResultHandler.Success(FakeMovieData.DEFAULT_MOVIE_DETAILS)

    override suspend fun getMovieCredits(id: Int) =
        checkError(MovieRemoteDsFunction.GetMovieCredits)
            ?: ResultHandler.Success(FakeMovieData.DEFAULT_MOVIE_CREDITS)

    override suspend fun getNowPlayingMovies(page: Int) =
        checkError(MovieRemoteDsFunction.GetNowPlayingMovies)
            ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getUpcomingMovies(page: Int) =
        checkError(MovieRemoteDsFunction.GetUpcomingMovies)
            ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getTopRatedMovies(page: Int) =
        checkError(MovieRemoteDsFunction.GetTopRatedMovies)
            ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getPopularMovies(page: Int) =
        checkError(MovieRemoteDsFunction.GetPopularMovies)
            ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getMovieGenres() =
        checkError(MovieRemoteDsFunction.GetMovieGenres) ?: ResultHandler.Success(FakeMovieData.GENRES)

    override suspend fun getMoviesByGenre(
        genres: String,
        sortBy: String,
        page: Int,
    ): ResultHandler<PageModel<Movie>> =
        checkError(MovieRemoteDsFunction.GetMoviesByGenre) ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun searchMovie(query: String, page: Int): ResultHandler<PageModel<Movie>> =
        checkError(MovieRemoteDsFunction.SearchMovie) ?: ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getMovieAccountStates(
        movieId: Int,
    ): ResultHandler<MovieAccountStates> =
        checkError(MovieRemoteDsFunction.GetMovieAccountStates)
            ?: ResultHandler.Success(
                MovieAccountStates(
                    id = movieId,
                    favorite = false,
                    watchlist = false
                )
            )
}
