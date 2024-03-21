package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS

class DeleteListUC(private val remoteDS: UserRemoteDS) {
    suspend operator fun invoke(
        listId: Int,
        sessionId: String,
    ) = remoteDS.deleteList(listId = listId, sessionId = sessionId)
}