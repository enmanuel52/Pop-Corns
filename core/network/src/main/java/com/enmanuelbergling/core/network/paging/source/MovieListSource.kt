package com.enmanuelbergling.core.network.paging.source

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource

internal class MovieListSource(remoteDS: UserRemoteDS, listId: Int) : GenericPagingSource<Movie>(
    request = { page ->
        when (val result = remoteDS.getWatchListMovies(listId, page)) {
            is ResultHandler.Error -> PageModel(emptyList(), 0)
            is ResultHandler.Success -> result.data ?: PageModel(emptyList(), 0)
        }
    }
)