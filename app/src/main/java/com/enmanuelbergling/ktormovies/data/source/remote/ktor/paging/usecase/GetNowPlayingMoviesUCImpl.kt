package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.model.GenericGetPagingUC
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.NowPlayingMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.core.model.movie.Movie

internal class GetNowPlayingMoviesUCImpl(pagingSource: NowPlayingMovieSource) :
    GenericGetPagingUC<MovieDTO, Movie>(pagingSource, MovieDTO::toModel)