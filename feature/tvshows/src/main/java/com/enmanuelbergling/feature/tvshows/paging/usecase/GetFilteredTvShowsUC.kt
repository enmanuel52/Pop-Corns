package com.enmanuelbergling.feature.tvshows.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.usecase.tv.SearchTvUC
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.feature.tvshows.paging.source.SearchTvSource
import kotlinx.coroutines.flow.Flow

class GetFilteredTvShowsUC(
    private val searchTvUC: SearchTvUC,
) {
    operator fun invoke(query: String): Flow<PagingData<TvShow>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { SearchTvSource(searchTvUC, query) }
    ).flow
}
