package com.enmanuelbergling.ktormovies.data.source.remote.mappers

import com.enmanuelbergling.ktormovies.data.source.remote.dto.auth.CreateSessionBody
import com.enmanuelbergling.ktormovies.domain.model.auth.CreateSessionPost

internal fun CreateSessionPost.asBody() = CreateSessionBody(
    username = username,
    password = password,
    requestToken = requestToken
)