package com.enmanuelbergling.feature.watchlists.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.user.AccountListsFilter
import com.enmanuelbergling.core.model.user.WatchList
import kotlinx.coroutines.flow.Flow

internal class GetUserWatchListsUC(private val userService: UserRemoteDS){
    operator fun invoke(filter: AccountListsFilter): Flow<PagingData<WatchList>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = {
            UserWatchListsSource(
                userService,
                filter
            )
        }
    ).flow
}