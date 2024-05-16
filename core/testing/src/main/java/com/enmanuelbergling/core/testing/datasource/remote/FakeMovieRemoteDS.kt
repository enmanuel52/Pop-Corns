package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.asPage
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.testing.data.FakeMovieData

class FakeMovieRemoteDS : MovieRemoteDS {
    override suspend fun getMovieDetails(id: Int) =
        ResultHandler.Success(FakeMovieData.DEFAULT_MOVIE_DETAILS)

    override suspend fun getMovieCredits(id: Int) =
        ResultHandler.Success(FakeMovieData.DEFAULT_MOVIE_CREDITS)

    override suspend fun getNowPlayingMovies(page: Int) =
        ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getUpcomingMovies(page: Int) =
        ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getTopRatedMovies(page: Int) =
        ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getPopularMovies(page: Int) =
        ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun getMovieGenres() = ResultHandler.Success(FakeMovieData.GENRES)

    override suspend fun getMoviesByGenre(
        genres: String,
        sortBy: String,
        page: Int,
    ): ResultHandler<PageModel<Movie>> = ResultHandler.Success(FakeMovieData.MOVIES.asPage())

    override suspend fun searchMovie(query: String, page: Int): ResultHandler<PageModel<Movie>> =
        ResultHandler.Success(FakeMovieData.MOVIES.asPage())
}