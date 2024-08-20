package com.enmanuelbergling.feature.actor.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS

internal class GetPopularActorsUC(private val remoteDS: ActorRemoteDS) {
    operator fun invoke() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { PopularActorsSource(remoteDS) }
    ).flow
}