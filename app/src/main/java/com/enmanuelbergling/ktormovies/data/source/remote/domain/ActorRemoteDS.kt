package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.ktormovies.domain.model.actor.ActorDetails
import com.enmanuelbergling.ktormovies.domain.model.actor.KnownMovie
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie

interface ActorRemoteDS : RemoteDataSource {
    suspend fun getActorDetails(id: Int): ResultHandler<ActorDetails>

    suspend fun getMoviesByActor(actorId: Int): ResultHandler<List<KnownMovie>>
}