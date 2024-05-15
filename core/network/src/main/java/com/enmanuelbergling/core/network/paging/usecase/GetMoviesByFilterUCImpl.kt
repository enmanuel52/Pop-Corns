package com.enmanuelbergling.core.network.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.core.network.paging.source.MoviesByFilterSource
import com.enmanuelbergling.core.network.paging.usecase.core.GetFilteredPagingFlowUC
import kotlinx.coroutines.flow.Flow

internal class GetMoviesByFilterUCImpl(private val moviesFilterService: MovieRemoteDS) :
    GetFilteredPagingFlowUC<Movie, MovieFilter> {
    override fun invoke(filter: MovieFilter): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MoviesByFilterSource(moviesFilterService, filter) }
    ).flow
}