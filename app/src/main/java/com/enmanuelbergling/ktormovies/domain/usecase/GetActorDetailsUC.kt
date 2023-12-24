package com.enmanuelbergling.ktormovies.domain.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.domain.ActorRemoteDS

class GetActorDetailsUC(
    private val remoteDS: ActorRemoteDS,
) {
    suspend operator fun invoke(id: Int) = remoteDS.getActorDetails(id)
}