package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source

import android.util.Log
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.WatchListDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.UserService
import com.enmanuelbergling.ktormovies.domain.TAG
import com.enmanuelbergling.ktormovies.domain.model.user.AccountListsFilter

internal class AccountListsSource(service: UserService, filter: AccountListsFilter) :
    GenericPagingSource<WatchListDTO>(
        request = { page ->
            Log.d(TAG, "list filter: $filter, $page")
            service.getAccountLists(filter.accountId, filter.sessionId, page)
        }
    )