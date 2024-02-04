package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.user.CreateListPost
import com.enmanuelbergling.ktormovies.domain.model.user.UserDetails
import com.enmanuelbergling.ktormovies.domain.model.user.WatchResponse

interface UserRemoteDS : RemoteDataSource {
    suspend fun getAccount(sessionId: String): ResultHandler<UserDetails>

    suspend fun createWatchList(
        listPost: CreateListPost,
        sessionId: String,
    ): ResultHandler<WatchResponse>
}