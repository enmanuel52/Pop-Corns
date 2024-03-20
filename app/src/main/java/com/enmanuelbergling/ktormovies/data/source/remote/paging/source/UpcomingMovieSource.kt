package com.enmanuelbergling.ktormovies.data.source.remote.paging.source

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.paging.source.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.MovieService

internal class UpcomingMovieSource(service: MovieService) :
    GenericPagingSource<MovieDTO>(
        request = service::getUpcomingMovies
    )