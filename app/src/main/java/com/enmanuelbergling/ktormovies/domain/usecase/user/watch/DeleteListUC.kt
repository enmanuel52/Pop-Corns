package com.enmanuelbergling.ktormovies.domain.usecase.user.watch

import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS

class DeleteListUC(private val remoteDS: UserRemoteDS) {
    suspend operator fun invoke(
        listId: Int,
        sessionId: String,
    ) = remoteDS.deleteList(listId = listId, sessionId = sessionId)
}