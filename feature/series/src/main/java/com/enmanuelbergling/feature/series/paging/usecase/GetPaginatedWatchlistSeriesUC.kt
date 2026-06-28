package com.enmanuelbergling.feature.series.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.feature.series.paging.source.WatchlistSeriesSource
import kotlinx.coroutines.flow.Flow

internal class GetPaginatedWatchlistSeriesUC(private val remoteDS: TvRemoteDS) {
    operator fun invoke(): Flow<PagingData<TvShow>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { WatchlistSeriesSource(remoteDS) }
    ).flow
}
