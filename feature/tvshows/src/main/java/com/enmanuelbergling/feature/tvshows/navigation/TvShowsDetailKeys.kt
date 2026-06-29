package com.enmanuelbergling.feature.tvshows.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal data class SeasonsKey(val tvShowId: Int) : NavKey

@Serializable
internal data class EpisodesKey(val tvShowId: Int, val seasonNumber: Int) : NavKey

@Serializable
internal data class EpisodeDetailsKey(
    val tvShowId: Int,
    val seasonNumber: Int,
    val episodeNumber: Int,
) : NavKey
