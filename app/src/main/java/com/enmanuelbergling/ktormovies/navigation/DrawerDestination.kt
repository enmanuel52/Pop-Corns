package com.enmanuelbergling.ktormovies.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistPlay
import androidx.compose.material.icons.automirrored.rounded.PlaylistPlay
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person2
import androidx.compose.ui.graphics.vector.ImageVector
import com.enmanuelbergling.feature.actor.navigation.ActorsDestination
import com.enmanuelbergling.feature.movies.navigation.MoviesDestination
import com.enmanuelbergling.feature.series.navigation.SeriesDestination
import com.enmanuelbergling.feature.watchlists.navigation.WatchListDestination
import com.enmanuelbergling.ktormovies.R

enum class DrawerDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    val unselectedIcon: ImageVector,
    val routes: List<Any>,
) {
    Home(
        R.string.home,
        Icons.Rounded.Home,
        Icons.Outlined.Home,
        listOf(MoviesDestination, SeriesDestination)
    ),
    Actors(
        R.string.actors,
        Icons.Rounded.Person2,
        Icons.Outlined.Person2,
        listOf(ActorsDestination)
    ),
    Lists(
        R.string.watch_lists,
        Icons.AutoMirrored.Rounded.PlaylistPlay, Icons.AutoMirrored.Outlined.PlaylistPlay, listOf(
            WatchListDestination
        )
    )
}

val DrawerDestination.loginRequired: Boolean
    get() = this in listOf(DrawerDestination.Lists)