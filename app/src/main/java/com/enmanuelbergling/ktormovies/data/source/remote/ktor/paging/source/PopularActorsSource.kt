package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source

import com.enmanuelbergling.ktormovies.data.source.remote.dto.actor.ActorDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.ActorService

internal class PopularActorsSource(service: ActorService) :
    GenericPagingSource<ActorDTO>(
        request = service::getPopularActors
    )