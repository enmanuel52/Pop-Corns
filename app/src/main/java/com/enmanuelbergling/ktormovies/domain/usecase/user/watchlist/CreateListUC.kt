package com.enmanuelbergling.ktormovies.domain.usecase.user.watchlist

import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS
import com.enmanuelbergling.ktormovies.domain.model.user.CreateListPost

class CreateListUC(private val remoteDS: UserRemoteDS) {

    suspend operator fun invoke(
        listPost: CreateListPost,
        sessionId: String,
    ) = remoteDS.createWatchList(listPost, sessionId)
}