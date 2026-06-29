package com.enmanuelbergling.core.network.dto.tv

import com.enmanuelbergling.core.network.dto.movie.CastDTO
import com.enmanuelbergling.core.network.dto.movie.CrewDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TvCreditsDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("cast")
    val cast: List<CastDTO>,
    @SerialName("crew")
    val crew: List<CrewDTO>,
)
