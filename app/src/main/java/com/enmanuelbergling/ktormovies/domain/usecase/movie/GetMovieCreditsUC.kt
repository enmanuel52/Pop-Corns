package com.enmanuelbergling.ktormovies.domain.usecase.movie

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS

class GetMovieCreditsUC(
    private val remoteDS: MovieRemoteDS
) {
    suspend operator fun invoke(id: Int) = remoteDS.getMovieCredits(id)
}