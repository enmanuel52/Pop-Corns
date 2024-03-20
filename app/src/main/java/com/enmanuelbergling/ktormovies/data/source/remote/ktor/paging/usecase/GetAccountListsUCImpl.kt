package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.WatchListDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.AccountListsSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.UserService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.GetFilteredPagingFlowUC
import com.enmanuelbergling.core.model.user.AccountListsFilter
import com.enmanuelbergling.core.model.user.WatchList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetAccountListsUCImpl(private val userService: UserService ):
    GetFilteredPagingFlowUC<WatchList, AccountListsFilter> {
    override fun invoke(filter: AccountListsFilter): Flow<PagingData<WatchList>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { AccountListsSource(userService, filter) }
    )
        .flow
        .map { pagingData -> pagingData.map(WatchListDTO::toModel) }
}