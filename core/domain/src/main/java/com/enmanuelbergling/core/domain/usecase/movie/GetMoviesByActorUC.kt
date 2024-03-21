package com.enmanuelbergling.core.domain.usecase.movie

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS

class GetMoviesByActorUC(
    private val remoteDS: ActorRemoteDS
){
    suspend operator fun invoke(actorId:Int) =  remoteDS.getMoviesByActor(actorId)
}