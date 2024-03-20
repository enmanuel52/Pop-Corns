package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.source

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.SearchService
import com.enmanuelbergling.core.model.movie.QueryString

internal class SearchMovieSource(service: SearchService, query: QueryString) :
    GenericPagingSource<MovieDTO>(
        request = { page ->
            service.searchMovie(query.query, page)
        }
    )