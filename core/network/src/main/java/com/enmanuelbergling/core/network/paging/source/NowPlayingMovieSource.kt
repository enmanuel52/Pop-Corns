package com.enmanuelbergling.core.network.paging.source

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource
import com.enmanuelbergling.core.network.ktor.service.MovieService

internal class NowPlayingMovieSource(remoteDS: MovieRemoteDS) :
    GenericPagingSource<Movie>(
        request = { page ->
            when (val result = remoteDS.getNowPlayingMovies(page)) {
                is ResultHandler.Error -> PageModel(emptyList(), 0)
                is ResultHandler.Success -> result.data ?: PageModel(emptyList(), 0)
            }
        }
    )