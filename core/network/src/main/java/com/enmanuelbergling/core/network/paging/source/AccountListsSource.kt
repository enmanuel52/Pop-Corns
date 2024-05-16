package com.enmanuelbergling.core.network.paging.source

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.user.AccountListsFilter
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.network.BuildConfig
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource

internal class AccountListsSource(service: UserRemoteDS, filter: AccountListsFilter) :
    GenericPagingSource<WatchList>(
        request = { page ->
            val result = service.getAccountLists(BuildConfig.ACCOUNT_ID, filter.sessionId, page)
            when (result) {
                is ResultHandler.Error -> PageModel(emptyList(), 0)
                is ResultHandler.Success -> result.data ?: PageModel(emptyList(), 0)
            }
        }
    )