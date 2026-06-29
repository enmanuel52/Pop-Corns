package com.enmanuelbergling.core.network.dto.tv

import com.enmanuelbergling.core.network.dto.movie.CastDTO
import com.enmanuelbergling.core.network.dto.movie.CrewDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EpisodeDetailsDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("episode_number")
    val episodeNumber: Int,
    @SerialName("season_number")
    val seasonNumber: Int,
    @SerialName("name")
    val name: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("still_path")
    val stillPath: String?,
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("vote_average")
    val voteAverage: Double = .0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    @SerialName("guest_stars")
    val guestStars: List<CastDTO> = emptyList(),
    @SerialName("crew")
    val crew: List<CrewDTO> = emptyList(),
)
