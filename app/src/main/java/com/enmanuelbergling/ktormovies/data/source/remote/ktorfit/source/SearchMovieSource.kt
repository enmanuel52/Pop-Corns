package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.source

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.SearchService

internal class SearchMovieSource(service: SearchService, query: String) :
    GenericPagingSource<MovieDTO>(
        request = { page ->
            service.searchMovie(query, page)
        }
    )