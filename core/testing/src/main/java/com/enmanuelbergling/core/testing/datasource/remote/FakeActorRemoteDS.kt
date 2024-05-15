package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS
import com.enmanuelbergling.core.model.actor.KnownMovie
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.testing.data.FakeActorData
import com.enmanuelbergling.core.testing.data.FakeMovieData

class FakeActorRemoteDS : ActorRemoteDS {
    override suspend fun getActorDetails(id: Int) =
        ResultHandler.Success(FakeActorData.ACTOR_DETAILS)

    override suspend fun getMoviesByActor(actorId: Int) =
        ResultHandler.Success(FakeMovieData.MOVIES.map { movie ->
            KnownMovie(
                id = movie.id,
                posterPath = movie.posterPath,
                title = movie.title,
                voteAverage = movie.voteAverage
            )
        })
}