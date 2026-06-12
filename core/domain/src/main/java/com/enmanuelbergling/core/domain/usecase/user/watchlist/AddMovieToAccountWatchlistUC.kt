package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS

class AddMovieToAccountWatchlistUC(private val remoteDS: UserRemoteDS) {
    suspend operator fun invoke(
        movieId: Int,
        sessionId: String,
    ) = remoteDS.addMovieToAccountWatchlist(movieId = movieId, sessionId = sessionId)
}
