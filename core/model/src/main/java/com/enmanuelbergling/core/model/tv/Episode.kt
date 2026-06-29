package com.enmanuelbergling.core.model.tv


data class Episode(
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
) {
    val duration: String
        get() = runtime?.let { minutes ->
            buildString {
                val hours = minutes / 60
                if (hours > 0) append("$hours h ")
                append("${minutes % 60} m")
            }
        } ?: "Unavailable"
}
