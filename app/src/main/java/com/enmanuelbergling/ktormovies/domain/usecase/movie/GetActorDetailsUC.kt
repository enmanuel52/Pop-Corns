package com.enmanuelbergling.ktormovies.domain.usecase.movie

import com.enmanuelbergling.ktormovies.data.source.remote.domain.ActorRemoteDS

class GetActorDetailsUC(
    private val remoteDS: ActorRemoteDS,
) {
    suspend operator fun invoke(id: Int) = remoteDS.getActorDetails(id)
}