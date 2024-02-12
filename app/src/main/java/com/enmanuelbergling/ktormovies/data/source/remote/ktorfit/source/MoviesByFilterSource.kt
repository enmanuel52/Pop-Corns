package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.source

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.FilterService
import com.enmanuelbergling.ktormovies.domain.model.movie.MovieFilter

internal class MoviesByFilterSource(service: FilterService, filter: MovieFilter) :
    GenericPagingSource<MovieDTO>(
        request = { page ->
            service.getMoviesByGenre(
                genres = filter.genres.map { it.id }.joinToString(","),
                sortBy = filter.sortBy.string,
                page = page
            )
        }
    )