package com.enmanuelbergling.core.network.paging.source

import com.enmanuelbergling.core.model.user.AccountListsFilter
import com.enmanuelbergling.core.network.BuildConfig
import com.enmanuelbergling.core.network.dto.user.watch.WatchListDTO
import com.enmanuelbergling.core.network.ktor.service.UserService
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource

internal class AccountListsSource(service: UserService, filter: AccountListsFilter) :
    GenericPagingSource<WatchListDTO>(
        request = { page ->
            service.getAccountLists(BuildConfig.ACCOUNT_ID, filter.sessionId, page)
        }
    )