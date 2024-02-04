package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.MovieListDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.AccountListsSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.MovieListSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.UserService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.user.AccountListsFilter
import com.enmanuelbergling.ktormovies.domain.model.user.MovieList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetAccountListsUCImpl(private val userService: UserService ): GetFilteredPagingFlowUC<MovieList, AccountListsFilter> {
    override fun invoke(filter: AccountListsFilter): Flow<PagingData<MovieList>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { AccountListsSource(userService, filter) }
    )
        .flow
        .map { pagingData -> pagingData.map(MovieListDTO::toModel) }
}