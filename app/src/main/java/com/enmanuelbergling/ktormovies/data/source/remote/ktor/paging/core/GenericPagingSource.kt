package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.model.PagingResponse

open class GenericPagingSource<Dto : Any>(
    private val request: suspend (page: Int) -> PagingResponse<Dto>,
) : PagingSource<Int, Dto>() {
    override fun getRefreshKey(state: PagingState<Int, Dto>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Dto> {
        val position = params.key ?: 1

        return try {
            val response = request(position)

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