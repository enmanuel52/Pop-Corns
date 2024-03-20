package com.enmanuelbergling.core.network.mappers

import com.enmanuelbergling.core.network.dto.auth.CreateSessionBody
import com.enmanuelbergling.core.model.auth.CreateSessionPost

internal fun CreateSessionPost.asBody() = CreateSessionBody(
    username = username,
    password = password,
    requestToken = requestToken
)