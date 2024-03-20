package com.enmanuelbergling.ktormovies.data.source.remote.paging.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.paging.usecase.core.GenericGetPagingUC
import com.enmanuelbergling.ktormovies.data.source.remote.paging.source.TopRatedMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.core.model.movie.Movie

internal class GetTopRatedMoviesUCImpl(pagingSource: TopRatedMovieSource) :
    GenericGetPagingUC<MovieDTO, Movie>(pagingSource, MovieDTO::toModel)