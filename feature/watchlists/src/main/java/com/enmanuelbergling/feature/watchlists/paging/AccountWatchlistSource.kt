package com.enmanuelbergling.feature.watchlists.paging

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.core.GenericPagingSource

internal class AccountWatchlistSource(remoteDS: UserRemoteDS, sessionId: String) : GenericPagingSource<Movie>(
    request = { page ->
        when (val result = remoteDS.getAccountWatchlistMovies(sessionId, page)) {
            is ResultHandler.Error -> PageModel(emptyList(), 0)
            is ResultHandler.Success -> result.data ?: PageModel(emptyList(), 0)
        }
    }
)