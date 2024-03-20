package com.enmanuelbergling.core.network.paging.source

import com.enmanuelbergling.core.network.dto.actor.ActorDTO
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource
import com.enmanuelbergling.core.network.ktor.service.ActorService

internal class PopularActorsSource(service: ActorService) :
    GenericPagingSource<ActorDTO>(
        request = service::getPopularActors
    )