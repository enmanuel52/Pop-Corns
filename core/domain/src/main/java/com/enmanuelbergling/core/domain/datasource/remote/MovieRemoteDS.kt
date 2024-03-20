package com.enmanuelbergling.core.domain.datasource.remote

import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieCredits
import com.enmanuelbergling.core.model.movie.MovieDetails

interface MovieRemoteDS : RemoteDataSource {

    suspend fun getMovieDetails(id: Int): ResultHandler<MovieDetails>

    suspend fun getMovieCredits(id: Int): ResultHandler<MovieCredits>

    /**
     * just first page*/
    suspend fun getNowPlayingMovies(): ResultHandler<List<Movie>>

    /**
     * just first page*/
    suspend fun getUpcomingMovies(): ResultHandler<List<Movie>>

    /**
     * just first page*/
    suspend fun getTopRatedMovies(): ResultHandler<List<Movie>>

    /**
     * just first page*/
    suspend fun getPopularMovies(): ResultHandler<List<Movie>>

    suspend fun getMovieGenres(): ResultHandler<List<Genre>>
}