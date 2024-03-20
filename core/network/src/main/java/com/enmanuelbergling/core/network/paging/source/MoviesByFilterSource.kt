package com.enmanuelbergling.core.network.paging.source

import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.core.model.movie.SortCriteria
import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.ktorfit.service.FilterService
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource

internal class MoviesByFilterSource(service: FilterService, filter: MovieFilter) :
    GenericPagingSource<MovieDTO>(
        request = { page ->
            service.getMoviesByGenre(
                genres = filter.genres.map { it.id }.joinToString(","),
                sortBy = filter.sortBy.stringValue,
                page = page
            )
        }
    )

val SortCriteria.stringValue: String
    get() = when (this) {
        SortCriteria.Popularity -> "popularity.desc"
        SortCriteria.VoteAverage -> "vote_average.desc"
        SortCriteria.VoteCount -> "vote_count.desc"
        SortCriteria.Revenue -> "revenue.desc"
    }