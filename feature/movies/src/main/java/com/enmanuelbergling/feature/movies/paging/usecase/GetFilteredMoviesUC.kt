package com.enmanuelbergling.feature.movies.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.core.model.movie.QueryString
import com.enmanuelbergling.feature.movies.paging.source.MoviesByFilterSource
import com.enmanuelbergling.feature.movies.paging.source.SearchMovieSource
import kotlinx.coroutines.flow.Flow

internal class GetFilteredMoviesUC(
    private val remoteDS: MovieRemoteDS,
    private val moviesFilterService: MovieRemoteDS,
) {
    operator fun invoke(filter: QueryString): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { SearchMovieSource(remoteDS, filter) }
    ).flow

    operator fun invoke(filter: MovieFilter): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MoviesByFilterSource(moviesFilterService, filter) }
    ).flow
}