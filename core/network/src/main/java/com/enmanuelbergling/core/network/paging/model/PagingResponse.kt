package com.enmanuelbergling.core.network.paging.model

interface PagingResponse<Dto : Any> {
    val totalPages: Int

    val results: List<Dto>
}