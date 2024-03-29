package com.enmanuelbergling.core.network.mappers

import com.enmanuelbergling.core.network.dto.user.UserDetailsDTO
import com.enmanuelbergling.core.network.dto.user.watch.CreateListBody
import com.enmanuelbergling.core.network.dto.user.watch.WatchListDTO
import com.enmanuelbergling.core.network.dto.user.watch.WatchResponseDTO
import com.enmanuelbergling.core.model.user.CreateListPost
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchResponse

internal fun UserDetailsDTO.toModel() = UserDetails(
    id = id, username = username, avatarPath = avatar.tmdb.avatarPath.orEmpty(), name = name
)

internal fun CreateListPost.asBody() = CreateListBody(name, description, language)

internal fun WatchResponseDTO.toModel() = WatchResponse(statusMessage)

internal fun WatchListDTO.toModel() = WatchList(
    description = description,
    favoriteCount = favoriteCount,
    id = id,
    itemCount = itemCount,
    iso6391 = iso6391,
    listType = listType,
    name = name,
    posterPath = posterPath
)