package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.MovieListSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.UserService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.GetFilteredPagingFlowUC
import com.enmanuelbergling.core.model.movie.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetMovieListUCImpl(private val userService: UserService) :
    GetFilteredPagingFlowUC<Movie, Int> {
    override fun invoke(filter: Int): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MovieListSource(userService, filter) }
    )
        .flow
        .map { pagingData -> pagingData.map(MovieDTO::toModel) }
}