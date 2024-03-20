package com.enmanuelbergling.ktormovies.data.source.remote.paging.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.paging.usecase.core.GenericGetPagingUC
import com.enmanuelbergling.ktormovies.data.source.remote.paging.source.PopularMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.core.model.movie.Movie

internal class GetPopularMoviesUCImpl(pagingSource: PopularMovieSource) :
    GenericGetPagingUC<MovieDTO, Movie>(pagingSource, MovieDTO::toModel)