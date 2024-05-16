package com.enmanuelbergling.core.domain.datasource.remote

import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.core.model.actor.ActorDetails
import com.enmanuelbergling.core.model.actor.KnownMovie
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler

interface ActorRemoteDS : RemoteDataSource {
    suspend fun getActorDetails(id: Int): ResultHandler<ActorDetails>

    suspend fun getMoviesByActor(actorId: Int): ResultHandler<List<KnownMovie>>

    suspend fun getPopularActors(page: Int): ResultHandler<PageModel<Actor>>
}