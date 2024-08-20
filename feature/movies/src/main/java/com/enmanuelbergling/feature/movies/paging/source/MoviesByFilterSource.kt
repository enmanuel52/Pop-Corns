package com.enmanuelbergling.feature.movies.paging.source

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.core.model.movie.SortCriteria
import com.enmanuelbergling.core.ui.core.GenericPagingSource

internal class MoviesByFilterSource(remoteDS: MovieRemoteDS, filter: MovieFilter) :
    GenericPagingSource<Movie>(
        request = { page ->
            val result = remoteDS.getMoviesByGenre(
                genres = filter.genres.map { it.id }.joinToString(","),
                sortBy = filter.sortBy.stringValue,
                page = page
            )

            when (result) {
                is ResultHandler.Error -> PageModel(emptyList(), 0)
                is ResultHandler.Success -> result.data ?: PageModel(emptyList(), 0)
            }
        }
    )

val SortCriteria.stringValue: String
    get() = when (this) {
        SortCriteria.Popularity -> "popularity.desc"
        SortCriteria.VoteAverage -> "vote_average.desc"
        SortCriteria.VoteCount -> "vote_count.desc"
        SortCriteria.Revenue -> "revenue.desc"
    }