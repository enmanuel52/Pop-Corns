package com.enmanuelbergling.ktormovies.domain.usecase.movie

import com.enmanuelbergling.ktormovies.data.source.remote.domain.MovieRemoteDS

class GetTopRatedMoviesUC(
    private val remoteDS: MovieRemoteDS
) {
    suspend operator fun invoke() = remoteDS.getTopRatedMovies()
}