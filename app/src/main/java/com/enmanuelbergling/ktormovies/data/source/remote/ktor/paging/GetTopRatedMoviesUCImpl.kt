package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.MovieService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.Movie
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetTopRatedMoviesUCImpl(private val service: MovieService) : GetPagingFlowUC<Movie> {
    override fun invoke(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { TopRatedMovieSource(service) }
        )
            .flow
            .map { pagingData -> pagingData.map { it.toModel() } }
}