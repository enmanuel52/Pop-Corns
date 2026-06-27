package com.enmanuelbergling.feature.series.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.feature.series.paging.source.AiringTodaySeriesSource
import com.enmanuelbergling.feature.series.paging.source.OnTheAirSeriesSource
import com.enmanuelbergling.feature.series.paging.source.PopularSeriesSource
import com.enmanuelbergling.feature.series.paging.source.TopRatedSeriesSource

internal class GetSectionSeriesUC(
    private val remoteDS: TvRemoteDS,
) {
    fun getPopular() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { PopularSeriesSource(remoteDS) }
    ).flow

    fun getTopRated() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { TopRatedSeriesSource(remoteDS) }
    ).flow

    fun getOnTheAir() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { OnTheAirSeriesSource(remoteDS) }
    ).flow

    fun getAiringToday() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { AiringTodaySeriesSource(remoteDS) }
    ).flow
}
