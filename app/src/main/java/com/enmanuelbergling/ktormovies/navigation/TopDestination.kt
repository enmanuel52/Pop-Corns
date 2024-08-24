package com.enmanuelbergling.ktormovies.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistPlay
import androidx.compose.material.icons.automirrored.rounded.PlaylistPlay
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Person2
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import com.enmanuelbergling.feature.actor.navigation.ActorsDestination
import com.enmanuelbergling.feature.movies.navigation.MoviesDestination
import com.enmanuelbergling.feature.series.navigation.SeriesDestination
import com.enmanuelbergling.feature.settings.navigation.SettingsDestination
import com.enmanuelbergling.feature.watchlists.navigation.WatchListDestination
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.core.ui.R as UiRes

enum class TopDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any,
) {
    Movies(
        R.string.movies,
        Icons.Rounded.Movie,
        Icons.Outlined.Movie,
        MoviesDestination
    ),
    Series(
        R.string.series,
        Icons.Rounded.Tv,
        Icons.Outlined.Tv,
        SeriesDestination
    ),
    Actors(
        R.string.actors,
        Icons.Rounded.Person2,
        Icons.Outlined.Person2,
        ActorsDestination
    ),
    Lists(
        R.string.watch_lists,
        Icons.AutoMirrored.Rounded.PlaylistPlay,
        Icons.AutoMirrored.Outlined.PlaylistPlay,
        WatchListDestination
    ),
    Settings(
        UiRes.string.settings,
        Icons.Rounded.Settings,
        Icons.Outlined.Settings,
        SettingsDestination
    );

    val loginRequired: Boolean
        get() = this in listOf(Lists)
}