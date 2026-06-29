package com.enmanuelbergling.feature.tvshows.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.feature.tvshows.paging.source.FavoriteTvShowsSource
import kotlinx.coroutines.flow.Flow

internal class GetPaginatedFavoriteTvShowsUC(private val remoteDS: TvRemoteDS) {
    operator fun invoke(): Flow<PagingData<TvShow>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { FavoriteTvShowsSource(remoteDS) }
    ).flow
}
