package com.enmanuelbergling.core.domain.usecase.tv

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS

class GetTvCreditsUC(
    private val remoteDS: TvRemoteDS,
) {
    suspend operator fun invoke(tvShowId: Int) = remoteDS.getTvCredits(tvShowId)
}
