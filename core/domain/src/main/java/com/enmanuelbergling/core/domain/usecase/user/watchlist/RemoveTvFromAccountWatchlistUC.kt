package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS

class RemoveTvFromAccountWatchlistUC(private val remoteDS: TvRemoteDS) {

    suspend operator fun invoke(tvShowId: Int) = remoteDS.removeTvFromAccountWatchlist(tvShowId)
}
