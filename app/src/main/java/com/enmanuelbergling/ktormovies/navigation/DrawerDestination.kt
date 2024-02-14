package com.enmanuelbergling.ktormovies.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person2
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.ui.graphics.vector.ImageVector
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.ui.screen.actor.navigation.ACTORS_SCREEN_ROUTE
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.navigation.LIST_SCREEN_ROUTE
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.MOVIES_SCREEN_ROUTE
import com.enmanuelbergling.ktormovies.ui.screen.series.navigation.SERIES_SCREEN_ROUTE

enum class DrawerDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    val unselectedIcon: ImageVector,
    val routes: List<String>,
) {
    Home(R.string.home, Icons.Rounded.Home, Icons.Outlined.Home, listOf(MOVIES_SCREEN_ROUTE, SERIES_SCREEN_ROUTE)),
    Actors(R.string.actors,Icons.Rounded.Person2, Icons.Outlined.Person2, listOf(ACTORS_SCREEN_ROUTE)),
    Lists(R.string.watch_lists,Icons.Rounded.PlaylistPlay, Icons.Outlined.PlaylistPlay, listOf(LIST_SCREEN_ROUTE))
}