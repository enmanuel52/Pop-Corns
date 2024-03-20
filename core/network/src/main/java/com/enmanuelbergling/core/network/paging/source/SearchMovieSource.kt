package com.enmanuelbergling.core.network.paging.source

import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource
import com.enmanuelbergling.core.network.ktorfit.service.SearchService
import com.enmanuelbergling.core.model.movie.QueryString

internal class SearchMovieSource(service: SearchService, query: QueryString) :
    GenericPagingSource<MovieDTO>(
        request = { page ->
            service.searchMovie(query.query, page)
        }
    )