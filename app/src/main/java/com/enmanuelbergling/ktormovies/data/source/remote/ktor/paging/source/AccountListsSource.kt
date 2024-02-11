package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source

import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.WatchListDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.UserService
import com.enmanuelbergling.ktormovies.domain.model.user.AccountListsFilter

internal class AccountListsSource(service: UserService, filter: AccountListsFilter) :
    GenericPagingSource<WatchListDTO>(
        request = { page ->
            service.getAccountLists(filter.accountId, filter.sessionId, page)
        }
    )