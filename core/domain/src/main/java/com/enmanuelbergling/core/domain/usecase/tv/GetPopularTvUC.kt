package com.enmanuelbergling.core.domain.usecase.tv

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS

class GetPopularTvUC(
    private val remoteDS: TvRemoteDS,
) {
    suspend operator fun invoke(page: Int = 1) = remoteDS.getPopularTv(page)
}
