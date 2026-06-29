package com.enmanuelbergling.core.model.tv

import com.enmanuelbergling.core.model.movie.Cast
import com.enmanuelbergling.core.model.movie.Crew


data class EpisodeDetails(
    val id: Int,
    val episodeNumber: Int,
    val seasonNumber: Int,
    val name: String,
    val overview: String,
    val stillPath: String?,
    val airDate: String?,
    val runtime: Int?,
    val voteAverage: Double,
    val voteCount: Int,
    val guestStars: List<Cast>,
    val crew: List<Crew>,
) {
    val airYear: String
        get() = airDate?.let {
            if (it.length > 3) it.substring(0..3)
            else it
        }.orEmpty()

    val duration: String
        get() = runtime?.let { minutes ->
            buildString {
                val hours = minutes / 60
                if (hours > 0) append("$hours h ")
                append("${minutes % 60} m")
            }
        } ?: "Unavailable"
}
