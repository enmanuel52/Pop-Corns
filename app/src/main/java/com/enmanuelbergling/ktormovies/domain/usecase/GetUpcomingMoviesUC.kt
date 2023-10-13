package com.enmanuelbergling.ktormovies.domain.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.domain.MovieRemoteDS

class GetUpcomingMoviesUC (
    private val remoteDS: MovieRemoteDS
) {
    suspend operator fun invoke()  = remoteDS.getUpcomingMovies()
}