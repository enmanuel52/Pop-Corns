package com.enmanuelbergling.core.network.dto.actor


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ActorDTO(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("gender")
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?
)