package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.model

interface PagingResponse<Dto : Any> {
    val totalPages: Int

    val results: List<Dto>
}