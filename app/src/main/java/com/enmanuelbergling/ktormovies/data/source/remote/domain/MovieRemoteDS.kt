package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.domain.model.movie.MovieCredits
import com.enmanuelbergling.ktormovies.domain.model.movie.MovieDetails
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

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
}