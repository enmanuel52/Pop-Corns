package com.enmanuelbergling.core.datastore.mappers

import com.enmanuelbergling.ktormovies.UserPref
import com.enmanuelbergling.core.model.user.UserDetails

internal fun UserPref.toModel() = UserDetails(
    id = id, username = username, avatarPath = avatarPath, name = name
)

internal fun UserDetails.toPreference() = UserPref.newBuilder()
    .setId(id)
    .setUsername(username)
    .setAvatarPath(avatarPath)
    .setName(name)
    .build()