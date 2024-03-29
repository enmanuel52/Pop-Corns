package com.enmanuelbergling.core.network.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.ktorfit.service.SearchService
import com.enmanuelbergling.core.network.paging.source.SearchMovieSource
import com.enmanuelbergling.core.network.mappers.toModel
import com.enmanuelbergling.core.network.paging.usecase.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.QueryString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetSearchMovieUCImpl(private val searchService: SearchService) :
    GetFilteredPagingFlowUC<Movie, QueryString> {
    override fun invoke(filter: QueryString): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { SearchMovieSource(searchService, filter) }
    )
        .flow
        .map { pagingData -> pagingData.map(MovieDTO::toModel) }
}