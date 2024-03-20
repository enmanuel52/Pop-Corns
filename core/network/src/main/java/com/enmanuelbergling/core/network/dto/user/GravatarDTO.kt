package com.enmanuelbergling.core.network.dto.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GravatarDTO(
    @SerialName("hash")
    val hash: String
)