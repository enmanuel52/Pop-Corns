package com.enmanuelbergling.ktormovies.domain.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.domain.MovieRemoteDS

class GetMovieCreditsUC(
    private val remoteDS: MovieRemoteDS
) {
    suspend operator fun invoke(id: Int) = remoteDS.getMovieCredits(id)
}