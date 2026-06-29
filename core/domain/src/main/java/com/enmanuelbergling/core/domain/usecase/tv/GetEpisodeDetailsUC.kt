package com.enmanuelbergling.core.domain.usecase.tv

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS

class GetEpisodeDetailsUC(
    private val remoteDS: TvRemoteDS,
) {
    suspend operator fun invoke(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) =
        remoteDS.getEpisodeDetails(tvShowId, seasonNumber, episodeNumber)
}
