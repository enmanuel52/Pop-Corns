package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS
import com.enmanuelbergling.core.model.actor.KnownMovie
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.asPage
import com.enmanuelbergling.core.testing.data.FakeActorData
import com.enmanuelbergling.core.testing.data.FakeMovieData

enum class ActorRemoteDsFunction {
    GetActorDetails,
    GetMoviesByActor,
    GetPopularActors
}

class FakeActorRemoteDS : ActorRemoteDS {

    private val errors = mutableMapOf<ActorRemoteDsFunction, NetworkException>()

    fun throwError(vararg errors: Pair<ActorRemoteDsFunction, NetworkException>) {
        this.errors.putAll(errors)
    }

    private fun <T> checkError(function: ActorRemoteDsFunction): ResultHandler<T>? =
        errors[function]?.let { ResultHandler.Error(it) }

    override suspend fun getActorDetails(id: Int) =
        checkError(ActorRemoteDsFunction.GetActorDetails)
            ?: ResultHandler.Success(FakeActorData.ACTOR_DETAILS)

    override suspend fun getMoviesByActor(actorId: Int) =
        checkError(ActorRemoteDsFunction.GetMoviesByActor) ?: ResultHandler.Success(FakeMovieData.MOVIES.map { movie ->
            KnownMovie(
                id = movie.id,
                posterPath = movie.posterPath,
                title = movie.title,
                voteAverage = movie.voteAverage
            )
        })

    override suspend fun getPopularActors(page: Int) =
        checkError(ActorRemoteDsFunction.GetPopularActors)
            ?: ResultHandler.Success(data = listOf(FakeActorData.ACTOR).asPage())
}
