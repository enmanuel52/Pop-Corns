package com.enmanuelbergling.ktormovies.data.source.remote.paging.model

interface PagingResponse<Dto : Any> {
    val totalPages: Int

    val results: List<Dto>
}