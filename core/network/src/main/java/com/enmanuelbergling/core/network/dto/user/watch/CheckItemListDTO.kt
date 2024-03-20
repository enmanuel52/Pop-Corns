package com.enmanuelbergling.core.network.dto.user.watch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CheckItemListDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("item_present")
    val itemPresent: Boolean
)