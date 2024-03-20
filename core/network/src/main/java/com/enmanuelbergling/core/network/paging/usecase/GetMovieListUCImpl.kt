package com.enmanuelbergling.core.network.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.paging.source.MovieListSource
import com.enmanuelbergling.core.network.ktor.service.UserService
import com.enmanuelbergling.core.network.mappers.toModel
import com.enmanuelbergling.core.network.paging.usecase.core.GetFilteredPagingFlowUC
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