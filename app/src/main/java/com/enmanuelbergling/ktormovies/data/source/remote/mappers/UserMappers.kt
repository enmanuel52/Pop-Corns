package com.enmanuelbergling.ktormovies.data.source.remote.mappers

import com.enmanuelbergling.ktormovies.data.source.remote.dto.user.UserDetailsDTO
import com.enmanuelbergling.ktormovies.domain.model.user.UserDetails

internal fun UserDetailsDTO.toModel() = UserDetails(
    id = id, username = username, avatarPath = avatar.tmdb.avatarPath.orEmpty(), name = name
)