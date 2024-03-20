package com.enmanuelbergling.core.network.paging.usecase

import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.paging.usecase.core.GenericGetPagingUC
import com.enmanuelbergling.core.network.paging.source.UpcomingMovieSource
import com.enmanuelbergling.core.network.mappers.toModel
import com.enmanuelbergling.core.model.movie.Movie

internal class GetUpcomingMoviesUCImpl(pagingSource: UpcomingMovieSource) :
    GenericGetPagingUC<MovieDTO, Movie>(pagingSource, MovieDTO::toModel)