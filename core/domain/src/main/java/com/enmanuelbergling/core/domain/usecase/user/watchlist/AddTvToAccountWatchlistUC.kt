package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS

class AddTvToAccountWatchlistUC(private val remoteDS: TvRemoteDS) {

    suspend operator fun invoke(seriesId: Int) = remoteDS.addTvToAccountWatchlist(seriesId)
}
