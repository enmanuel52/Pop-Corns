package com.enmanuelbergling.core.domain.usecase.user.favorite

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS

class AddTvToFavoritesUC(private val remoteDS: TvRemoteDS) {

    suspend operator fun invoke(tvShowId: Int) = remoteDS.addTvToFavorites(tvShowId)
}
