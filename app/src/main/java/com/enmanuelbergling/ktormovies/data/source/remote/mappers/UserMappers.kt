package com.enmanuelbergling.ktormovies.data.source.remote.mappers

import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.UserDetailsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.CreateListBody
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.MovieListDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.watch.WatchResponseDTO
import com.enmanuelbergling.ktormovies.domain.model.user.CreateListPost
import com.enmanuelbergling.ktormovies.domain.model.user.MovieList
import com.enmanuelbergling.ktormovies.domain.model.user.UserDetails
import com.enmanuelbergling.ktormovies.domain.model.user.WatchResponse

internal fun UserDetailsDTO.toModel() = UserDetails(
    id = id, username = username, avatarPath = avatar.tmdb.avatarPath.orEmpty(), name = name
)

internal fun CreateListPost.asBody() = CreateListBody(name, description, language)

internal fun WatchResponseDTO.toModel() = WatchResponse(statusMessage)

internal fun MovieListDTO.toModel() = MovieList(
    description = description,
    favoriteCount = favoriteCount,
    id = id,
    itemCount = itemCount,
    iso6391 = iso6391,
    listType = listType,
    name = name,
    posterPath = posterPath
)