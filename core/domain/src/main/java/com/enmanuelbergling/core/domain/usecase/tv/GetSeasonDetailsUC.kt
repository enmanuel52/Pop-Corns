package com.enmanuelbergling.core.domain.usecase.tv

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS

class GetSeasonDetailsUC(
    private val remoteDS: TvRemoteDS,
) {
    suspend operator fun invoke(seriesId: Int, seasonNumber: Int) =
        remoteDS.getSeasonDetails(seriesId, seasonNumber)
}
