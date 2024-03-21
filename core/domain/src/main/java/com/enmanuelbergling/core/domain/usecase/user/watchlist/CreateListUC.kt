package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.user.CreateListPost

class CreateListUC(private val remoteDS: UserRemoteDS) {

    suspend operator fun invoke(
        listPost: CreateListPost,
        sessionId: String,
    ) = remoteDS.createWatchList(listPost, sessionId)
}