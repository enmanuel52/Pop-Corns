package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.source

import android.util.Log
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MoviePageDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.SearchService
import com.enmanuelbergling.ktormovies.domain.TAG
import com.enmanuelbergling.ktormovies.domain.model.movie.QueryString
import io.ktor.client.call.body

internal class SearchMovieSource(service: SearchService, query: QueryString) :
    GenericPagingSource<MovieDTO>(
        request = { page ->
            service.searchMovie(query.query, page)
        }
    )