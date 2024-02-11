package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.FilterService
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.source.MovieByGenresSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.movie.Genre
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetMovieByGenresUCImpl(private val filterService: FilterService) :
    GetFilteredPagingFlowUC<Movie, List<Genre>> {
    override fun invoke(filter: List<Genre>): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MovieByGenresSource(filterService, filter) }
    )
        .flow
        .map { pagingData -> pagingData.map(MovieDTO::toModel) }
}