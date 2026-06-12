package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS

class GetAccountWatchlistMoviesUC(private val remoteDS: UserRemoteDS) {
    suspend operator fun invoke(sessionId: String, page: Int) =
        remoteDS.getAccountWatchlistMovies(sessionId, page)
}