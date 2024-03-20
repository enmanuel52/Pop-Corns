package com.enmanuelbergling.core.network.paging.source

import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.paging.source.core.GenericPagingSource
import com.enmanuelbergling.core.network.ktor.service.UserService

internal class MovieListSource(service: UserService, listId: Int) : GenericPagingSource<MovieDTO>(
    request = { page -> service.getListDetails(listId, page) }
)