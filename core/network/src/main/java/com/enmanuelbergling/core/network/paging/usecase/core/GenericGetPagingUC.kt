package com.enmanuelbergling.core.network.paging.usecase.core

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * it's useful to provide a pagination as [Flow]*/
open class GenericGetPagingUC<Dto : Any, Model : Any>(
    private val genericPagingSource: GenericPagingSource<Dto>,
    private val mapper: Dto.() -> Model,
) :
    GetPagingFlowUC<Model> {
    override fun invoke(): Flow<PagingData<Model>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { genericPagingSource }
        )
            .flow
            .map { pagingData -> pagingData.map(mapper) }
}