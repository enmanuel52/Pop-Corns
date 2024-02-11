package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.SearchService
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.source.SearchMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.domain.model.movie.QueryString
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