package com.enmanuelbergling.core.domain.usecase.movie

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS

class GetMovieAccountStatesUC(private val remoteDS: MovieRemoteDS) {
    suspend operator fun invoke(movieId: Int, sessionId: String) =
        remoteDS.getMovieAccountStates(movieId, sessionId)
}