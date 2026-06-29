package com.enmanuelbergling.feature.tvshows.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.feature.tvshows.paging.source.AiringTodayTvShowsSource
import com.enmanuelbergling.feature.tvshows.paging.source.OnTheAirTvShowsSource
import com.enmanuelbergling.feature.tvshows.paging.source.PopularTvShowsSource
import com.enmanuelbergling.feature.tvshows.paging.source.TopRatedTvShowsSource

internal class GetSectionTvShowsUC(
    private val remoteDS: TvRemoteDS,
) {
    fun getPopular() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { PopularTvShowsSource(remoteDS) }
    ).flow

    fun getTopRated() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { TopRatedTvShowsSource(remoteDS) }
    ).flow

    fun getOnTheAir() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { OnTheAirTvShowsSource(remoteDS) }
    ).flow

    fun getAiringToday() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { AiringTodayTvShowsSource(remoteDS) }
    ).flow
}
