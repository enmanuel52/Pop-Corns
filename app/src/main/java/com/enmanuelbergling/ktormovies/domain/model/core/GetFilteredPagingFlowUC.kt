package com.enmanuelbergling.ktormovies.domain.model.core

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GetFilteredPagingFlowUC<T : Any, Filter : Any> {

    operator fun invoke(filter: Filter): Flow<PagingData<T>>
}