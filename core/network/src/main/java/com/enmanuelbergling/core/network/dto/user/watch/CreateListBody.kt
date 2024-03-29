package com.enmanuelbergling.core.network.dto.user.watch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateListBody(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("language")
    val language: String = "en"
)