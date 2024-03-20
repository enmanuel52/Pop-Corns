package com.enmanuelbergling.core.network.dto.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AvatarDTO(
    @SerialName("gravatar")
    val gravatar: GravatarDTO,
    @SerialName("tmdb")
    val tmdb: TmdbDTO
)