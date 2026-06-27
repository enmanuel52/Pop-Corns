package com.enmanuelbergling.core.network.dto.tv

import com.enmanuelbergling.core.network.dto.movie.GenreDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TvShowDetailsDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("genres")
    val genres: List<GenreDTO> = emptyList(),
    @SerialName("first_air_date")
    val firstAirDate: String = "",
    @SerialName("last_air_date")
    val lastAirDate: String = "",
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int = 0,
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int = 0,
    @SerialName("status")
    val status: String = "",
    @SerialName("tagline")
    val tagline: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = .0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    @SerialName("seasons")
    val seasons: List<SeasonDTO> = emptyList(),
)
