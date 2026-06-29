package com.enmanuelbergling.feature.tvshows.paging.source

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.ui.core.GenericPagingSource

internal class PopularTvShowsSource(remoteDS: TvRemoteDS) :
    GenericPagingSource<TvShow>(
        request = { page ->
            when (val result = remoteDS.getPopularTv(page)) {
                is ResultHandler.Error -> PageModel(emptyList(), 0)
                is ResultHandler.Success -> result.data ?: PageModel(emptyList(), 0)
            }
        }
    )
