package com.enmanuelbergling.core.domain.usecase.movie

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS

class GetActorDetailsUC(
    private val remoteDS: ActorRemoteDS,
) {
    suspend operator fun invoke(id: Int) = remoteDS.getActorDetails(id)
}