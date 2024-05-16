package com.enmanuelbergling.core.network.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.network.paging.source.SearchMovieSource
import com.enmanuelbergling.core.network.paging.usecase.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.QueryString
import kotlinx.coroutines.flow.Flow

internal class GetSearchMovieUCImpl(private val remoteDS: MovieRemoteDS) :
    GetFilteredPagingFlowUC<Movie, QueryString> {
    override fun invoke(filter: QueryString): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { SearchMovieSource(remoteDS, filter) }
    ).flow
}