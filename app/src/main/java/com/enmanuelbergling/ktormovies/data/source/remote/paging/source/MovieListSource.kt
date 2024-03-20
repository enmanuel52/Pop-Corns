package com.enmanuelbergling.ktormovies.data.source.remote.paging.source

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.paging.source.core.GenericPagingSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.UserService

internal class MovieListSource(service: UserService, listId: Int) : GenericPagingSource<MovieDTO>(
    request = { page -> service.getListDetails(listId, page) }
)