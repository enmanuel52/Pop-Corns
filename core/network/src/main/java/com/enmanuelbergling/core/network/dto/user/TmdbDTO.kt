package com.enmanuelbergling.core.network.dto.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TmdbDTO(
    @SerialName("avatar_path")
    val avatarPath: String?
)