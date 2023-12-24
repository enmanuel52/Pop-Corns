package com.enmanuelbergling.ktormovies.domain.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.domain.ActorRemoteDS

class GetMoviesByActorUC(
    private val remoteDS: ActorRemoteDS
){
    suspend operator fun invoke(actorId:Int) =  remoteDS.getMoviesByActor(actorId)
}