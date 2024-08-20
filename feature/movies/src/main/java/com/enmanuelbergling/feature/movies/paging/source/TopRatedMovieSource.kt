package com.enmanuelbergling.feature.movies.paging.source

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.core.GenericPagingSource

internal class TopRatedMovieSource(remoteDS: MovieRemoteDS) :
    GenericPagingSource<Movie>(
        request = { page ->
            when (val result = remoteDS.getTopRatedMovies(page)) {
                is ResultHandler.Error -> PageModel(emptyList(), 0)
                is ResultHandler.Success -> result.data ?: PageModel(emptyList(), 0)
            }
        }
    )