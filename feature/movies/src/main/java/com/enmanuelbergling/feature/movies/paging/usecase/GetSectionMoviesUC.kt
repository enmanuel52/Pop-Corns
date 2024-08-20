package com.enmanuelbergling.feature.movies.paging.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.feature.movies.paging.source.NowPlayingMovieSource
import com.enmanuelbergling.feature.movies.paging.source.PopularMovieSource
import com.enmanuelbergling.feature.movies.paging.source.TopRatedMovieSource
import com.enmanuelbergling.feature.movies.paging.source.UpcomingMovieSource

internal class GetSectionMoviesUC(
    private val remoteDS: MovieRemoteDS,
) {
    fun getNowPlaying() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { NowPlayingMovieSource(remoteDS) }
    ).flow

    fun getPopular() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { PopularMovieSource(remoteDS) }
    ).flow

    fun getTopRated() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { TopRatedMovieSource(remoteDS) }
    ).flow

    fun getUpcoming() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { UpcomingMovieSource(remoteDS) }
    ).flow
}