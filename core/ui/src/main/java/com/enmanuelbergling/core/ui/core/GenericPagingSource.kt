package com.enmanuelbergling.core.ui.core

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.enmanuelbergling.core.common.util.TAG
import com.enmanuelbergling.core.model.core.PageModel

open class GenericPagingSource<Model : Any>(
    private val request: suspend (page: Int) -> PageModel<Model>,
) : PagingSource<Int, Model>() {
    override fun getRefreshKey(state: PagingState<Int, Model>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Model> {
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
            Log.d(TAG, "load: ${exception.message}")
            LoadResult.Error(exception)
        }
    }
}