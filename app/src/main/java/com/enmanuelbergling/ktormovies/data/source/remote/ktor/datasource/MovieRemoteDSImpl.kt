package com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource

import com.enmanuelbergling.ktormovies.data.source.remote.domain.MovieRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.MovieService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.MovieCredits
import com.enmanuelbergling.ktormovies.domain.model.MovieDetails
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

internal class MovieRemoteDSImpl(private val service: MovieService) : MovieRemoteDS {
    override suspend fun getMovieDetails(id: Int): ResultHandler<MovieDetails> = safeKtorCall {
        service.getMovieDetails(id).toModel()
    }

    override suspend fun getMovieCredits(id: Int): ResultHandler<MovieCredits> = safeKtorCall {
        service.getMovieCredits(id).toModel()
    }
}