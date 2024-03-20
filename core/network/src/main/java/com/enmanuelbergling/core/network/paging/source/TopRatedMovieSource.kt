package com.enmanuelbergling.core.network.paging.source

import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource
import com.enmanuelbergling.core.network.ktor.service.MovieService

internal class TopRatedMovieSource(service: MovieService) :
    GenericPagingSource<MovieDTO>(
        request = service::getTopRatedMovies
    )