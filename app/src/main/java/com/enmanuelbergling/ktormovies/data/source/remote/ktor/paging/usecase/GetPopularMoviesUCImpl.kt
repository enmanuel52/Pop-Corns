package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.model.GenericGetPagingUC
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.PopularMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie

internal class GetPopularMoviesUCImpl(pagingSource: PopularMovieSource) :
    GenericGetPagingUC<MovieDTO, Movie>(pagingSource, MovieDTO::toModel)