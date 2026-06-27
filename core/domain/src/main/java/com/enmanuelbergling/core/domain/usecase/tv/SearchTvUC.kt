package com.enmanuelbergling.core.domain.usecase.tv

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS

class SearchTvUC(
    private val remoteDS: TvRemoteDS,
) {
    suspend operator fun invoke(query: String, page: Int) = remoteDS.searchTv(query, page)
}
