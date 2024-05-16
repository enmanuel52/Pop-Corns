package com.enmanuelbergling.core.network.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.network.paging.source.MovieListSource
import com.enmanuelbergling.core.network.paging.usecase.core.GetFilteredPagingFlowUC
import kotlinx.coroutines.flow.Flow

internal class GetMovieListUCImpl(private val remoteDS: UserRemoteDS) :
    GetFilteredPagingFlowUC<Movie, Int> {
    override fun invoke(filter: Int): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MovieListSource(remoteDS, filter) }
    ).flow
}