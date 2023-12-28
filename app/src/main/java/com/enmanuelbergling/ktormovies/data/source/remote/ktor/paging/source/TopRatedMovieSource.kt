package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.MovieService

internal class TopRatedMovieSource(service: MovieService) :
    GenericPagingSource<MovieDTO>(
        request = service::getTopRatedMovies
    )