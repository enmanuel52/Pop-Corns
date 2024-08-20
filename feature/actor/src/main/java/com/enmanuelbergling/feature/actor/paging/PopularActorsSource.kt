package com.enmanuelbergling.feature.actor.paging

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.ui.core.GenericPagingSource

internal class PopularActorsSource(remoteDS: ActorRemoteDS) :
    GenericPagingSource<Actor>(
        request = { page ->
            when (val result = remoteDS.getPopularActors(page)) {
                is ResultHandler.Error -> PageModel(emptyList(), 0)
                is ResultHandler.Success -> result.data ?: PageModel(emptyList(), 0)
            }
        }
    )