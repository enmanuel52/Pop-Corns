package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.testing.data.FakeMovieData

class FakeMovieRemoteDS : MovieRemoteDS {
    override suspend fun getMovieDetails(id: Int) =
        ResultHandler.Success(FakeMovieData.DEFAULT_MOVIE_DETAILS)

    override suspend fun getMovieCredits(id: Int) =
        ResultHandler.Success(FakeMovieData.DEFAULT_MOVIE_CREDITS)

    override suspend fun getNowPlayingMovies() = ResultHandler.Success(FakeMovieData.MOVIES)

    override suspend fun getUpcomingMovies() = ResultHandler.Success(FakeMovieData.MOVIES)

    override suspend fun getTopRatedMovies() = ResultHandler.Success(FakeMovieData.MOVIES)

    override suspend fun getPopularMovies() = ResultHandler.Success(FakeMovieData.MOVIES)
    override suspend fun getMovieGenres() = ResultHandler.Success(FakeMovieData.GENRES)
}