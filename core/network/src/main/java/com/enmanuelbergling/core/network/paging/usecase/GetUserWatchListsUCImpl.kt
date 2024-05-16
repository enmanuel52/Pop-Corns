package com.enmanuelbergling.core.network.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.user.AccountListsFilter
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.network.paging.source.UserWatchListsSource
import com.enmanuelbergling.core.network.paging.usecase.core.GetFilteredPagingFlowUC
import kotlinx.coroutines.flow.Flow

internal class GetUserWatchListsUCImpl(private val userService: UserRemoteDS) :
    GetFilteredPagingFlowUC<WatchList, AccountListsFilter> {
    override fun invoke(filter: AccountListsFilter): Flow<PagingData<WatchList>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { UserWatchListsSource(userService, filter) }
    ).flow
}