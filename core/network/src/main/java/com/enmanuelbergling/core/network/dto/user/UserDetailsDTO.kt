package com.enmanuelbergling.core.network.dto.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserDetailsDTO(
    @SerialName("avatar")
    val avatar: AvatarDTO,
    @SerialName("id")
    val id: Int,
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("iso_3166_1")
    val iso31661: String,
    @SerialName("name")
    val name: String,
    @SerialName("include_adult")
    val includeAdult: Boolean,
    @SerialName("username")
    val username: String
)