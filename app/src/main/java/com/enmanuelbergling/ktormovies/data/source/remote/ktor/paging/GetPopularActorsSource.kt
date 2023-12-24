package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.ActorService
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetPopularActorsSourceImpl(private val service: ActorService) :
    GetPagingFlowUC<Actor> {
    override fun invoke(): Flow<PagingData<Actor>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { PopularActorsSource(service) }
        )
            .flow
            .map { pagingData -> pagingData.map { it.toModel() } }
}