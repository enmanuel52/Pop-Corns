package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.enmanuelbergling.ktormovies.data.source.remote.dto.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.MovieService

internal class TopRatedMovieSource(private val service: MovieService) :
    PagingSource<Int, MovieDTO>() {
    override fun getRefreshKey(state: PagingState<Int, MovieDTO>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDTO> {

        val position = params.key ?: 1

        return try {
            val response = service.getTopRatedMovies(position)

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