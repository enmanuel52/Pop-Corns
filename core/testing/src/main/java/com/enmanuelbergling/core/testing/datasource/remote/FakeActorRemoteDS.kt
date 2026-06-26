package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS
import com.enmanuelbergling.core.model.actor.KnownMovie
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.asPage
import com.enmanuelbergling.core.testing.data.FakeActorData
import com.enmanuelbergling.core.testing.data.FakeMovieData

class FakeActorRemoteDS : ActorRemoteDS {

    var errorToThrow: NetworkException? = null

    private fun <T> checkError(): ResultHandler<T>? = errorToThrow?.let { ResultHandler.Error(it) }

    override suspend fun getActorDetails(id: Int) =
        checkError() ?: ResultHandler.Success(FakeActorData.ACTOR_DETAILS)

    override suspend fun getMoviesByActor(actorId: Int) =
        checkError() ?: ResultHandler.Success(FakeMovieData.MOVIES.map { movie ->
            KnownMovie(
                id = movie.id,
                posterPath = movie.posterPath,
                title = movie.title,
                voteAverage = movie.voteAverage
            )
        })

    override suspend fun getPopularActors(page: Int) =
        checkError() ?: ResultHandler.Success(data = listOf(FakeActorData.ACTOR).asPage())
}
