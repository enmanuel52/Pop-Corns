package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.user.UserDetails

interface UserRemoteDS: RemoteDataSource {
    suspend fun getAccount(sessionId: String): ResultHandler<UserDetails>
}