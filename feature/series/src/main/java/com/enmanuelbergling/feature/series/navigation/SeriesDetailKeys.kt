package com.enmanuelbergling.feature.series.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal data class SeasonsKey(val seriesId: Int) : NavKey

@Serializable
internal data class EpisodesKey(val seriesId: Int, val seasonNumber: Int) : NavKey

@Serializable
internal data class EpisodeDetailsKey(
    val seriesId: Int,
    val seasonNumber: Int,
    val episodeNumber: Int,
) : NavKey
