package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.ktormovies.domain.model.actor.ActorDetails
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

interface ActorRemoteDS: RemoteDataSource {
    suspend fun getActorDetails(id: Int): ResultHandler<ActorDetails>
}