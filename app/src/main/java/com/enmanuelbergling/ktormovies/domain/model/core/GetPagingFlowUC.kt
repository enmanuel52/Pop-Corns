package com.enmanuelbergling.ktormovies.domain.model.core

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GetPagingFlowUC<T : Any> {

    operator fun invoke(): Flow<PagingData<T>>
}