package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.source

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.FilterService
import com.enmanuelbergling.ktormovies.domain.model.movie.Genre

internal class MovieByGenresSource(service: FilterService, genres: List<Genre>) : GenericPagingSource<MovieDTO>(
    request = { page ->
        service.getMoviesByGenre(
            genres.joinToString(","), page
        )
    }
)