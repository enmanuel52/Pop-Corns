package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.model.GenericGetPagingUC
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.TopRatedMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie

internal class GetTopRatedMoviesUCImpl(pagingSource: TopRatedMovieSource) :
    GenericGetPagingUC<MovieDTO, Movie>(pagingSource, MovieDTO::toModel)