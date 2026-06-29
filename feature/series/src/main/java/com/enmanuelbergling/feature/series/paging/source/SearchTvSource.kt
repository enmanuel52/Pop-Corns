package com.enmanuelbergling.feature.series.paging.source

import com.enmanuelbergling.core.domain.usecase.tv.SearchTvUC
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.ui.core.GenericPagingSource

internal class SearchTvSource(searchTvUC: SearchTvUC, query: String) :
    GenericPagingSource<TvShow>(
        request = { page ->
            when (val result = searchTvUC(query, page)) {
                is ResultHandler.Error -> PageModel(emptyList(), 0)
                is ResultHandler.Success -> result.data ?: PageModel(emptyList(), 0)
            }
        }
    )
