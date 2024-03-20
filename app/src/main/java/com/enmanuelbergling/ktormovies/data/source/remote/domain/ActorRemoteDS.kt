package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.core.model.actor.ActorDetails
import com.enmanuelbergling.core.model.actor.KnownMovie
import com.enmanuelbergling.core.model.core.ResultHandler

interface ActorRemoteDS : RemoteDataSource {
    suspend fun getActorDetails(id: Int): ResultHandler<ActorDetails>

    suspend fun getMoviesByActor(actorId: Int): ResultHandler<List<KnownMovie>>
}