package com.enmanuelbergling.core.domain.usecase.movie

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS

class GetUpcomingMoviesUC (
    private val remoteDS: MovieRemoteDS
) {
    suspend operator fun invoke()  = remoteDS.getUpcomingMovies()
}