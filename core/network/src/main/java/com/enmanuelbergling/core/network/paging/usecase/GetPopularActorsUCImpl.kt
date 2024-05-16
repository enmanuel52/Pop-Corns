package com.enmanuelbergling.core.network.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.core.network.paging.source.PopularActorsSource
import com.enmanuelbergling.core.network.paging.usecase.core.GetPagingFlowUC

internal class GetPopularActorsUCImpl(private val pagingSource: PopularActorsSource) :
    GetPagingFlowUC<Actor> {
    override fun invoke() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { pagingSource }
    ).flow
}