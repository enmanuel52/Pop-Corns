package com.enmanuelbergling.core.domain.usecase.tv

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS

class GetSeasonDetailsUC(
    private val remoteDS: TvRemoteDS,
) {
    suspend operator fun invoke(tvShowId: Int, seasonNumber: Int) =
        remoteDS.getSeasonDetails(tvShowId, seasonNumber)
}
