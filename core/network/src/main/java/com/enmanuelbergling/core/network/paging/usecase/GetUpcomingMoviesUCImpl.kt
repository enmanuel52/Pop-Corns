package com.enmanuelbergling.core.network.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.network.paging.source.UpcomingMovieSource
import com.enmanuelbergling.core.network.paging.usecase.core.GetPagingFlowUC

internal class GetUpcomingMoviesUCImpl(private val pagingSource: UpcomingMovieSource) :
    GetPagingFlowUC<Movie> {
    override fun invoke() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { pagingSource }
    ).flow
}