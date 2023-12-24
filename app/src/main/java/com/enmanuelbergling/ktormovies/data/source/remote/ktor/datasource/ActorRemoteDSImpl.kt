package com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource

import com.enmanuelbergling.ktormovies.data.source.remote.domain.ActorRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.ActorService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.actor.ActorDetails
import com.enmanuelbergling.ktormovies.domain.model.actor.KnownMovie
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie

internal class ActorRemoteDSImpl(private val service: ActorService): ActorRemoteDS {
    override suspend fun getActorDetails(id: Int): ResultHandler<ActorDetails> = safeKtorCall {
        service.getActorDetails(id).toModel()
    }

    override suspend fun getMoviesByActor(actorId: Int): ResultHandler<List<KnownMovie>> =safeKtorCall{
        service.getMoviesByActor(actorId).cast.map { it.toModel() }
    }
}