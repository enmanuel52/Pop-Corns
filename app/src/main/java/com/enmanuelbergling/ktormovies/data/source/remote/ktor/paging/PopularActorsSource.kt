package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.enmanuelbergling.ktormovies.data.source.remote.dto.actor.ActorDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.ActorService

internal class PopularActorsSource(private val service: ActorService) :
    PagingSource<Int, ActorDTO>() {
    override fun getRefreshKey(state: PagingState<Int, ActorDTO>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ActorDTO> {

        val position = params.key ?: 1

        return try {
            val response = service.getPopularActors(position)

            val nextKey = if (response.totalPages == position) {
                null
            } else {
                position + 1
            }

            LoadResult.Page(
                data = response.results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}