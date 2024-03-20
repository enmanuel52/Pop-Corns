package com.enmanuelbergling.core.network.ktor.datasource

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.network.ktor.service.MovieService
import com.enmanuelbergling.core.network.mappers.toModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieCredits
import com.enmanuelbergling.core.model.movie.MovieDetails

internal class MovieRemoteDSImpl(private val service: MovieService) : MovieRemoteDS {
    override suspend fun getMovieDetails(id: Int): ResultHandler<MovieDetails> = safeKtorCall {
        service.getMovieDetails(id).toModel()
    }

    override suspend fun getMovieCredits(id: Int): ResultHandler<MovieCredits> = safeKtorCall {
        service.getMovieCredits(id).toModel()
    }

    override suspend fun getNowPlayingMovies(): ResultHandler<List<Movie>> = safeKtorCall {
        service.getNowPlayingMovies(1).results.map { it.toModel() }
    }

    override suspend fun getUpcomingMovies(): ResultHandler<List<Movie>> = safeKtorCall {
        service.getUpcomingMovies(1).results.map { it.toModel() }
    }

    override suspend fun getTopRatedMovies(): ResultHandler<List<Movie>> = safeKtorCall {
        service.getTopRatedMovies(1).results.map { it.toModel() }
    }

    override suspend fun getPopularMovies(): ResultHandler<List<Movie>> = safeKtorCall {
        service.getPopularMovies(1).results.map { it.toModel() }
    }

    override suspend fun getMovieGenres(): ResultHandler<List<Genre>> = safeKtorCall {
        service.getMovieGenres().genres.map { it.toModel() }
    }
}