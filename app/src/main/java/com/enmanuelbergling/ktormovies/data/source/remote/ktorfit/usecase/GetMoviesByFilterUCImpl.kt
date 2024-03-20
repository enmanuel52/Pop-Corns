package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.FilterService
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.source.MoviesByFilterSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.GetFilteredPagingFlowUC
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetMoviesByFilterUCImpl(private val filterService: FilterService) :
    GetFilteredPagingFlowUC<Movie, MovieFilter> {
    override fun invoke(filter: MovieFilter): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MoviesByFilterSource(filterService, filter) }
    )
        .flow
        .map { pagingData -> pagingData.map(MovieDTO::toModel) }
}