package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS

class RemoveMovieFromAccountWatchlistUC(private val remoteDS: UserRemoteDS) {

    suspend operator fun invoke(
        movieId: Int,
    ) = remoteDS.removeMovieFromAccountWatchlist(movieId = movieId)
}
